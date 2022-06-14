local table = {}
table["function"] = "turtle.inspectDown()"
table["target"] = "client"
local result,downTable = turtle.inspectDown()
table["result"] =tostring(result)
table["block"] = downTable
return table
