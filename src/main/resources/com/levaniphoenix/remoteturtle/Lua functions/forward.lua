local table = {}
table["function"] = "turtle.forward()"
table["target"] = "client"
local forwardResult = turtle.forward()
table["result"] = tostring(forwardResult)

local upResult,upTable = turtle.inspectUp()
table["upResult"] = upResult
table["upBlock"] = upTable

local downResult,downTable = turtle.inspectDown()
table["downResult"] = downResult
table["downBlock"] = downTable

local inspectResult,inspectTable = turtle.inspect()
table["inspectResult"] = inspectResult
table["frontBlock"] = inspectTable
return table
