local table = {}
table["function"] = "turtle.down()"
table["target"] = "client"
local result = turtle.down()
table["result"] = tostring(result)

local downResult,downTable = turtle.inspectDown()
table["downResult"] = downResult
table["downBlock"] = downTable

local inspectResult,inspectTable = turtle.inspect()
table["inspectResult"] = inspectResult
table["frontBlock"] = inspectTable

local upResult,upTable = turtle.inspectUp()
table["upResult"] = upResult
table["upBlock"] = upTable

return table