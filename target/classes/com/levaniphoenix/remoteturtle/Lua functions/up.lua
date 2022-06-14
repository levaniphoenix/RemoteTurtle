local table = {}
table["function"] = "turtle.up()"
table["target"] = "client"
local result = turtle.up()
table["result"] = tostring(result)

local inspectResult,inspectTable = turtle.inspect()
table["inspectResult"] = inspectResult
table["frontBlock"] = inspectTable

local upResult,upTable = turtle.inspectUp()
table["upResult"] = upResult
table["upBlock"] = upTable

return table