local allPairs = { a = {}, b = {} }

local dp2 = { { 2 } }
local dp6 = { { 6 } }
local allPackets = { dp2, dp6 }
for line in io.lines("./data/input_13.txt") do
    allPackets[#allPackets + 1] = load("return " .. line:gsub("%[", "{"):gsub("%]", "}"))()
end

local function comparePackets(a, b)
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
                comparisonResult = comparePackets(a[i], b[i])
                --print("comparison", comparisonResult)
                if comparisonResult ~= 0 then
                    return comparisonResult
                end
            else return a[i] == nil and -1 or 1
            end
        end
        return 0
    elseif type(a) == "table" and type(b) == "number" then
        return comparePackets(a, { b })
    elseif type(a) == "number" and type(b) == "table" then
        return comparePackets({ a }, b)
    end
end

--[[
local result = 0
for i, pair in ipairs(allPairs) do
    result = result + (comparePackets(pair[1], pair[2]) == -1 and i or 0)
end
--]]

local function compareBoolean(a, b)
    return comparePackets(a, b) == -1
end

local function printPacket(p)
    io.write("[")
    for _, v in ipairs(p) do
        if type(v) == "number" then
            io.write(v)
        else
            printPacket(v)
        end
        if _ < #p then io.write(",") end
    end
    io.write("]")
    if p == dp2 or p == dp6 then io.write(" <== divider") end
end

table.sort(allPackets, compareBoolean)

local result = 1
for _, p in ipairs(allPackets) do
    if p == dp2 or p == dp6 then
        result = result * _
    end
    printPacket(p)
    print()
end
print("Decoder key is", result)
