local function printNode(node, printNeighbors)
    io.write("********* " .. node.char .. " " .. node.height .. " " .. node.x .. "," .. node.y .. " ")
    if printNeighbors then
        io.write(" Neighbors: ")
        for _, child in ipairs(node.neighbors) do
            printNode(child, false)
        end
    end
end

local function printMap(map)
    for _, row in ipairs(map) do
        for i = 1, #row do
            -- printNode(row[i], true)
            io.write(_ .. " " .. i .. " ")
            printNode(allNodes[_][i])
            print()
        end
    end
end

local allPairs = { a = {}, b = {} }

local currentPair = {}
local index = 1
for line in io.lines("./data/input_13.txt") do
    if index < 3 then
        local packet = load("return " .. line:gsub("%[", "{"):gsub("%]", "}"))()
        currentPair[index] = packet
    end
    if index == 2 then
        allPairs[#allPairs + 1] = currentPair
        currentPair = {}
    end
    index = (index + 1) % 3
end

local function compareValues(a, b)
    if type(a) == "number" and type(b) == "number" then
        --print("Both are numbers. a:" .. a .. ", b:" .. b)
        return (a < b and -1) or (a > b and 1 or 0)
    elseif type(a) == "table" and type(b) == "table" then
        --print("Both are tables")
        local maxSize = #a > #b and #a or #b
        --local decisionFound = false
        for i = 1, maxSize do
            --print("for loop " .. i .. " / " .. maxSize)
            local comparisonResult
            if a[i] ~= nil and b[i] ~= nil then
                comparisonResult = compareValues(a[i], b[i])
                --print("comparison", comparisonResult)
                if comparisonResult ~= 0 then
                    return comparisonResult
                end
            else return a[i] == nil and -1 or 1
            end
        end
        return 0
    elseif type(a) == "table" and type(b) == "number" then
        return compareValues(a, { b })
    elseif type(a) == "number" and type(b) == "table" then
        return compareValues({ a }, b)
    end
end

--[[
for _,p in ipairs(allPairs) do
    print(#p, #p[1], #p[2])
end
--]]
--local pair = allPairs[2]
--print(compareValues(pair[1], pair[2]))

---[[
local result = 0
for i, pair in ipairs(allPairs) do
    result = result + (compareValues(pair[1], pair[2]) == -1 and i or 0)
end
print("Sum of indices of ordered pairs", result)
--]]
