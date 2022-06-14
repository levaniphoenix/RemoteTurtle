local table = {}
table["function"] = "turtle.inspect()"
table["target"] = "client"
local result,blockTable = turtle.inspect()
table["result"] =tostring(result)
table["block"] = blockTable
return table