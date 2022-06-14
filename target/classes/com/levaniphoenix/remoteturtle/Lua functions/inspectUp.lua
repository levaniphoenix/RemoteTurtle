local table = {}
table["function"] = "turtle.inspectUp()"
table["target"] = "client"
local result,upTable = turtle.inspectUp()
table["result"] =tostring(result)
table["block"] = upTable
return table