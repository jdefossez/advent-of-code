List = {}

function List.new()
    return { first = 0, last = -1 }
end

function List.pushLeft(list, value)
    local first = list.first - 1
    list.first = first
    list[first] = value
end

function List.pushRight(list, value)
    local last = list.last + 1
    list.last = last
    list[last] = value
end

function List.popLeft(list)
    local first = list.first
    if first > list.last then error("list is empty") end
    local value = list[first]
    list[first] = nil -- to allow garbage collection
    list.first = first + 1
    return value
end

function List.popRight(list)
    local last = list.last
    if list.first > last then error("list is empty") end
    local value = list[last]
    list[last] = nil -- to allow garbage collection
    list.last = last - 1
    return value
end

function List.size(list)
    return list.last - list.first + 1
end

return List

--[[
local list = List.new()

print(List.size(list))
List.pushRight(list, "A")
print(List.size(list))
List.pushRight(list, "B")
print(List.size(list))
List.pushRight(list, "C")

print(List.size(list))
print(List.popLeft(list))
print(List.size(list))
print(List.popLeft(list))
print(List.size(list))
print(List.popLeft(list))
print(List.size(list))
--]]