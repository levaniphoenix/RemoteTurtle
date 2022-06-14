local table = {}
table["function"] = "turtle.turnRight()"
table["target"] = "client"

local result = turtle.turnRight()
table["result"] = tostring(result)

local inspectResult,inspectTable = turtle.inspect()
table["inspectResult"] = inspectResult
table["frontBlock"] = inspectTable

return table