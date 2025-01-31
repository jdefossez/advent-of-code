local rocks = {}
rocks[1] = { name = "Hor. bar", width = 4, height = 1, units = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } } } -- horizontal bar
rocks[2] = { name = "Plus", width = 3, height = 3, units = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 1 } } } -- plus
rocks[3] = { name = "Rev. L", width = 3, height = 3, units = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 2, 1 }, { 2, 2 } } } -- reverse L
rocks[4] = { name = "Vert. bar", width = 1, height = 4, units = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } } } -- vertical bar
rocks[5] = { name = "Square", width = 2, height = 2, units = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } } } -- square

local file = assert(io.open("./data/input_17.txt", "r"))
local line = file:read("*line")
file:close()

local jets = {}
for i = 1, #line do
    jets[#jets + 1] = line:sub(i, i) == "<" and -1 or 1
end

local chamber = { maxHeight = 0 }
local maxHeigthsSequence = {}

local function getNextRock()
    local i = -1
    return function()
        i = i + 1
        --print("rock", rocks[1 + i % #rocks].name)
        return rocks[1 + i % #rocks]
    end
end

local function getNextJet()
    local i = -1
    return function()
        i = i + 1
        --print("jet ", jets[1 + i % #jets])
        return jets[1 + i % #jets]
    end
end

local function moveRockAtInitialPlace(rock)
    rock["origin"] = { x = 3, y = chamber.maxHeight + 4 }
    --print("Init chamber from ", chamber.maxHeight + 1, "to", chamber.maxHeight + 3 + rock.height)
    for h = chamber.maxHeight + 1, chamber.maxHeight + rock.height + 3 do
        if not chamber[h] then chamber[h] = { false, false, false, false, false, false, false } end
    end
end

local function canRockGoLeft(rock)
    if rock.origin.x - 1 == 0 then -- collide with left side of the chamber
        return false
    end

    for _, u in ipairs(rock.units) do -- check if a unit would collide with a unit of a rock already present in the chamber
        if chamber[rock.origin.y + u[2]][rock.origin.x + u[1] - 1] then
            return false
        end
    end

    return true
end

local function canRockGoRight(rock)
    if rock.origin.x + rock.width >= 8 then -- collide with left side of the chamber
        return false
    end

    for _, u in ipairs(rock.units) do -- check if a unit would collide with a unit of a rock already present in the chamber
        if chamber[rock.origin.y + u[2]][rock.origin.x + u[1] + 1] then
            return false
        end
    end

    return true
end

local function jetPushesRock(rock, jet)
    if jet < 0 and canRockGoLeft(rock) or jet > 0 and canRockGoRight(rock) then
        rock.origin.x = rock.origin.x + jet
    end
end

local function canRockGoDown(rock)
    for _, u in ipairs(rock.units) do
        -- is there allready a rock unit under the current unit
        if rock.origin.y + u[2] - 1 == 0 -- rock would collide with the chamber bottom
            or (chamber[rock.origin.y + u[2] - 1] and chamber[rock.origin.y + u[2] - 1][rock.origin.x + u[1]]) then
            -- something blocks the falling
            return false
        end
    end
    return true
end

local function makeRockFall(rock)
    if canRockGoDown(rock) then
        rock.origin.y = rock.origin.y - 1
        return false
    else
        -- add the rock units positions in the chamber data
        local previousMaxHeight = chamber.maxHeight
        for _, u in ipairs(rock.units) do
            -- print("trying to insert a unit in chamber, row ", rock.origin.y + u[2])
            if not chamber[rock.origin.y + u[2]] then
                chamber[rock.origin.y + u[2]] = { false, false, false, false, false, false, false }
            end
            chamber[rock.origin.y + u[2]][rock.origin.x + u[1]] = true
            if rock.origin.y + u[2] > chamber.maxHeight then chamber.maxHeight = rock.origin.y + u[2] end
        end
        maxHeigthsSequence[#maxHeigthsSequence + 1] = chamber.maxHeight - previousMaxHeight
        return true
    end
end

local function isRockIn(rock, x, y)
    for _, u in ipairs(rock.units) do
        if rock.origin.x + u[1] == x and rock.origin.y + u[2] == y then return true end
    end
    return false
end

local function printChamber(rock)
    local hMax = math.max(chamber.maxHeight, rock.origin.y + rock.height - 1)
    for h = hMax, 1, -1 do
        local l = ""
        for x = 1, 7 do
            l = l .. ((isRockIn(rock, x, h) or chamber[h][x]) and "#" or ".")
        end
        print("|" .. l .. "|")
    end
    print("+-------+")
end


local function longestRepeatedSubstring(str)
    local n = #str
    local LCSRe = {}
    for i = 1, n + 1 do
        LCSRe[i] = {}
    end

    local res = ""
    local res_length = 0

    local index = 0
    for i = 1, n do
        for j = i + 1, n do
            if (str:sub(i - 1, i - 1) == str:sub(j - 1, j - 1)
                and LCSRe[i - 1][j - 1] < (j - i)) then
                LCSRe[i][j] = LCSRe[i - 1][j - 1] + 1;

                if (LCSRe[i][j] > res_length) then
                    res_length = LCSRe[i][j];
                    index = math.max(i, index);
                end
            else
                LCSRe[i][j] = 0;
            end
        end
    end

    if (res_length > 0) then
        for i = index - res_length + 1, index do
            res = res .. str:sub(i - 1, i - 1)
        end
    end

    return res;
end

local function main(nbRocks)
    local nextRock = getNextRock()
    local nextJet = getNextJet()
    for i = 1, nbRocks do
        local rock = nextRock()
        --print("Rock number", i, rock.name)
        moveRockAtInitialPlace(rock)
        --print("Origin: ", rock.origin.x, rock.origin.y)
        --printChamber(rock)
        repeat
            -- jet pushes rock
            jetPushesRock(rock, nextJet())

            --printChamber(rock)
            -- rock falls 1 unit
            local rockRests = makeRockFall(rock)
            --print("Origin: ", rock.origin.x, rock.origin.y)

            --printChamber(rock)
        until rockRests
    end
end

local nbRocksTotal = 1000000000000
main(3442)

local output = table.concat(maxHeigthsSequence)
print(output)
local longestSequence = longestRepeatedSubstring(output)
local longestSequenceHeight = 0
for i = 1, #longestSequence do
    longestSequenceHeight = longestSequenceHeight + tonumber(longestSequence:sub(i, i))
end
print(longestSequence)
print("Cumulate height is", longestSequenceHeight)
local firstOccurenceIndex = output:find(longestSequence)

local prefixHeight = 0
for i = 1, firstOccurenceIndex-1 do
    prefixHeight = prefixHeight + tonumber(output:sub(i, i))
end
print("First occurence at:", firstOccurenceIndex, "Height before sequence start:", prefixHeight)



local suffixLength = (nbRocksTotal - firstOccurenceIndex) % #longestSequence
local suffixHeight = 0
for i = 1, suffixLength do
    suffixHeight = suffixHeight + tonumber(longestSequence:sub(i, i))
end

local nbOccurencesSubsequence = math.floor((nbRocksTotal - firstOccurenceIndex + 1) / #longestSequence)
print("Length of subsequence:", #longestSequence,
    "Prefix height is " .. prefixHeight,
    "Height of subsequence is:" .. longestSequenceHeight,
    "Subsequence present " .. nbOccurencesSubsequence .. " times",
    "Rest " .. suffixLength .. " rocks, for an height of " .. suffixHeight
)

local mesuredHeight = 0
for i = 1, #output do
    mesuredHeight = mesuredHeight + tonumber(output:sub(i, i))
end
print("Total mesured height is ", mesuredHeight)
print("Total calculated height is", prefixHeight + longestSequenceHeight*nbOccurencesSubsequence + suffixHeight)


-- 1526744186040 pas ça
-- The principle is good, but the fonction returning the longest repeating subsequence is not ok
-- I've found the answer by getting a repeating sequence manually and calculating the result
