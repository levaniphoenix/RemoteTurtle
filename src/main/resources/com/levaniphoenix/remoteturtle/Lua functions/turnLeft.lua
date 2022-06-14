local table = {}
table["function"] = "turtle.turnLeft()"
table["target"] = "client"

local result = turtle.turnLeft()
table["result"] = tostring(result)

local inspectResult,inspectTable = turtle.inspect()
table["inspectResult"] = inspectResult
table["frontBlock"] = inspectTable

return table