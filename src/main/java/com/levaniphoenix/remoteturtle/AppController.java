package com.levaniphoenix.remoteturtle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;
import spark.utils.IOUtils;

import javafx.scene.text.Text;


public class AppController {
    @FXML
    private SubScene subScene;
    @FXML
    private Text text;

    private Group root;

    public static Box turtle = new Box(10.0f, 10.0f, 10.0f);
    public static Direction direction = Direction.South;

    private final ArrayList<Block> sceneBlocks = new ArrayList<Block>();

    final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Translate cTrans = new Translate(0, 0, -200);
    private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS); //yaw
    private final Rotate xRotate = new Rotate(0, Rotate.X_AXIS); //pitch

    private ClientEndpoint clientEndpoint;
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @FXML
    protected void initialize() throws URISyntaxException {

        camera.setFarClip(1000);

        camera.getTransforms().addAll (
                yRotate,
                xRotate,
                new Rotate(-20, Rotate.X_AXIS),
                cTrans
        );

        subScene.setFill(Color.CADETBLUE);
        subScene.setCamera(camera);

        turtle.setTranslateX(0);
        turtle.setTranslateY(0);
        turtle.setMaterial(new PhongMaterial(Color.RED));

        root = new Group(turtle);
        subScene.setRoot(root);

        subScene.setOnMouseMoved( new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                for( var block : sceneBlocks){
                    if (block.isHover()){
                        text.setText(block.getBlockName());
                        return;
                    }
                }
                text.setText("");
            }
        });

        clientEndpoint = new ClientEndpoint(new URI("ws://localhost:4567"));
        clientEndpoint.setGroup(root);

        try {
            loadBlocks();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ClientEndpoint.MessageHandler messageHandler = new ClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {
                System.out.println("client got " + message);
                JSONObject json = new JSONObject(message);
                if(json.get("target").hashCode() == "client".hashCode()){

                    if(json.get("function").hashCode() == "turtle.forward()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        updateTurtleLocation();
                        placeCubes(json);
                    }

                    if(json.get("function").hashCode() == "turtle.turnRight()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        turnRight(json);
                    }

                    if(json.get("function").hashCode() == "turtle.turnLeft()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        turnLeft(json);
                    }

                    if(json.get("function").hashCode() == "turtle.inspectDown()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        placeCube(json, "down");
                    }
                    if(json.get("function").hashCode() == "turtle.inspectUp()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        placeCube(json, "up");
                    }

                    if(json.get("function").hashCode() == "turtle.inspect()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        placeCube(json, "front");
                    }

                    if(json.get("function").hashCode() == "turtle.up()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        goUp();
                        placeCubes(json);
                    }
                    if(json.get("function").hashCode() == "turtle.down()".hashCode() && json.get("result").hashCode() == "true".hashCode()){
                        goDown();
                        placeCubes(json);
                    }

                }

            }
        };

        clientEndpoint.addMessageHandler(messageHandler);
        getTurtleProperties();
    }

    @FXML
    protected void forwardMessage(){
        String func = loadFile("Lua functions/forward.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        System.out.println(json);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void turnLeftMessage(){
        String func = loadFile("Lua functions/turnLeft.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }
    @FXML
    protected void turnRightMessage(){
        String func = loadFile("Lua functions/turnRight.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void upMessage(){
        String func = loadFile("Lua functions/up.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void downMessage(){
        String func = loadFile("Lua functions/down.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void detectFrontMessage(){
        String func = loadFile("Lua functions/inspect.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void detectDownMessage(){
        String func = loadFile("Lua functions/inspectDown.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }
    @FXML
    protected void detectUpMessage(){
        String func = loadFile("Lua functions/inspectUp.lua");
        JSONObject json = new JSONObject();
        json.put("target", "turtle");
        json.put("func" ,func);
        clientEndpoint.sendMessage(json.toString());
    }

    @FXML
    protected void OnMousePressed(MouseEvent me){
        //mousePosX = me.getSceneX();
    }

    @FXML
    protected void scrollHandle(ScrollEvent event){
    }

    @FXML
    protected void handleMouseMovement(MouseEvent event){
        setYaw(event.getSceneX());
        setPitch(-event.getSceneY());
    }

    @FXML
    protected void handleKeyPress(KeyEvent key){

//        var siny = Math.sin(xRotate.getAngle());
//        var cosy = Math.cos(xRotate.getAngle());
//        var sinp = Math.sin(yRotate.getAngle());
//        var cosp = Math.cos(yRotate.getAngle());
//        var speed = 2;
//
//        if( key.getCode() == KeyCode.W){
//            double dx = Math.sin(Math.toRadians(yRotate.getAngle()));
//            double dz = Math.cos(Math.toRadians(xRotate.getAngle()));
//            setLocation(cTrans.getX() + dx*speed, cTrans.getY(), cTrans.getZ() + dz*speed);
//
//        }
    }

    private void updateTurtleLocation(){

        if(direction == Direction.South) {
            turtle.setTranslateZ(turtle.getTranslateZ() + 10f);
            camera.setTranslateZ(turtle.getTranslateZ() + 10f);
        }
        if (direction == Direction.North){
            turtle.setTranslateZ(turtle.getTranslateZ() - 10f);
            camera.setTranslateZ(turtle.getTranslateZ() - 10f);
        }

        if (direction == Direction.East){
            turtle.setTranslateX(turtle.getTranslateX() - 10f);
            camera.setTranslateX(turtle.getTranslateX() - 10f);
        }
        if (direction == Direction.West){
            turtle.setTranslateX(turtle.getTranslateX() + 10f);
            camera.setTranslateX(turtle.getTranslateX() + 10f);
        }
    }

    private void turnLeft(JSONObject json){
        if(direction == Direction.South) {
            direction = Direction.East;
        }
        else if(direction == Direction.East){
            direction = Direction.North;
        }else if(direction == Direction.North){
            direction = Direction.West;
        }else if(direction == Direction.West){
        direction = Direction.South;
        }
        if(json.getBoolean("inspectResult")){
            var js = new JSONObject();
            js.put("block",json.get("frontBlock"));
            placeCube(js, "front");
        }
    }
    private void turnRight(JSONObject json){
        if(direction == Direction.South) {
            direction = Direction.West;
        }else if(direction == Direction.West){
            direction = Direction.North;
        }else if(direction == Direction.North){
            direction = Direction.East;
        }else if(direction == Direction.East){
            direction = Direction.South;
        }
        if(json.getBoolean("inspectResult")){
            var js = new JSONObject();
            js.put("block",json.get("frontBlock"));
            placeCube(js, "front");
        }
    }
    private void goUp(){
        turtle.setTranslateY(turtle.getTranslateY() - 10f);
        camera.setTranslateY(turtle.getTranslateY() - 10f);
    }

    private void goDown(){
        turtle.setTranslateY(turtle.getTranslateY() + 10f);
        camera.setTranslateY(turtle.getTranslateY() + 10f);
    }
    private void placeCube(JSONObject json, String dir) {
        Block block = new Block(10.0f, 10.0f, 10.0f);

        block.setTranslateX(turtle.getTranslateX());
        block.setTranslateZ(turtle.getTranslateZ());
        block.setTranslateY(turtle.getTranslateY());
        if (Objects.equals(dir, "down")) {
            block.setTranslateY(turtle.getTranslateY() + 10f);
        }

        if (Objects.equals(dir, "up"))
            block.setTranslateY(turtle.getTranslateY() - 10f);

        if (Objects.equals(dir, "front")) {
            if (direction == Direction.South) {
                block.setTranslateZ(turtle.getTranslateZ() + 10f);
            }

            if (direction == Direction.North) {
                block.setTranslateZ(turtle.getTranslateZ() - 10f);
            }

            if (direction == Direction.East) {
                block.setTranslateX(turtle.getTranslateX() - 10f);
            }

            if (direction == Direction.West) {
                block.setTranslateX(turtle.getTranslateX() + 10f);
            }

        }
        Object name = new JSONObject(json.get("block").toString()).get("name");
        block.setBlockName(name.toString());
        block.setMaterial(new PhongMaterial(Color.hsb(name.hashCode() % 360, 1, 1.0)));

        sceneBlocks.add(block);

        try {
            Database.addBlock(name.toString(), block.getTranslateX(), block.getTranslateY(), block.getTranslateZ());
        } catch (SQLException e) {
            logger.error("could not add the block to db",e);
            e.printStackTrace();
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                root.getChildren().add(block);
            }
        });
    }
    private void placeCubes(JSONObject json){
        if(json.getBoolean("upResult")){
            var js = new JSONObject();
            js.put("block",json.get("upBlock"));
            placeCube(js, "up");
        }

        if(json.getBoolean("downResult")){
            var js = new JSONObject();
            js.put("block",json.get("downBlock"));
            placeCube(js, "down");
        }

        if(json.getBoolean("inspectResult")){
            var js = new JSONObject();
            js.put("block",json.get("frontBlock"));
            placeCube(js, "front");
        }

    }
    public void setLocation(double x, double y, double z) {
        cTrans.setX(x);
        cTrans.setY(y);
        cTrans.setZ(z);
    }
    public void setYaw(double angle) {
        yRotate.setAngle(normalizeAngle(angle));
    }

    public void setPitch(double angle) {
        xRotate.setAngle(normalizeAngle(angle));
    }

    private double normalizeAngle(double angle) {
        return angle > 0 ? angle % 360 : (360 + angle % 360) % 360;
    }

    private String loadFile(String path){
        InputStream is;
        is = AppController.class.getResourceAsStream(path);
        assert is != null;
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    private void loadBlocks() throws SQLException {
        logger.info("loading blocks from the database");
        ResultSet resultSet = Database.getAllBlocks();
        while (resultSet.next()){
            var x = resultSet.getDouble("x");
            var y = resultSet.getDouble("y");
            var z = resultSet.getDouble("z");
            var name = resultSet.getString("name");
            Block block = new Block(10f,10f,10f);
            block.setTranslateX(x);
            block.setTranslateY(y);
            block.setTranslateZ(z);
            block.setBlockName(name);
            block.setMaterial( new PhongMaterial(Color.hsb(name.hashCode()%360,1,1.0)));
            sceneBlocks.add(block);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    root.getChildren().add(block);
                }
            });
        }
    }
    private void getTurtleProperties(){
        Properties properties = new Properties();
        try (var inputStream = AppController.class.getResourceAsStream("turtle.properties")){
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String readDirection = properties.getProperty("turtle.direction");
        direction = Direction.valueOf(readDirection);
        turtle.setTranslateX(Double.parseDouble(properties.getProperty("turtle.x")));
        turtle.setTranslateY(Double.parseDouble(properties.getProperty("turtle.y")));
        turtle.setTranslateZ(Double.parseDouble(properties.getProperty("turtle.z")));

        camera.setTranslateX(Double.parseDouble(properties.getProperty("turtle.x")));
        camera.setTranslateY(Double.parseDouble(properties.getProperty("turtle.y")));
        camera.setTranslateZ(Double.parseDouble(properties.getProperty("turtle.z")));
    }
}
