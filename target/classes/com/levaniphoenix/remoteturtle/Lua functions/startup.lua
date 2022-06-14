os.loadAPI("json")
local ws,err = http.websocket("ws://localhost:4567")
print(ws)
print(err)
if ws then
    print("got connetction")
    while true do
        local msg = ws.receive()
        local obj = json.decode(msg)
          if obj["target"] == "turtle" then
            local func = loadstring(obj["func"])
            local table = func()
            
            jsontable = json.encode(table)
            
            ws.send(jsontable) 
          end
      end
end