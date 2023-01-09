local rocks = {}
rocks[1] = { name = "Hor. bar", width = 4, height = 1, units = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } } } -- horizontal bar
rocks[2] = { name = "Plus", width = 3, height = 3, units = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 1 } } } -- plus
rocks[3] = { name = "Rev. L", width = 3, height = 3, units = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 2, 1 }, { 2, 2 } } } -- reverse L
rocks[4] = { name = "Vert. bar", width = 1, height = 4, units = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } } } -- vertical bar
rocks[5] = { name = "Square", width = 2, height = 2, units = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } } } -- square

local file = assert(io.open("./data/test_17.txt", "r"))
local line = file:read("*line")
file:close()

local jets = {}
for i = 1, #line do
    jets[#jets + 1] = line:sub(i, i) == "<" and -1 or 1
end

local chamber = { maxHeight = 0 }

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
        --io.write((chamber.maxHeight - previousMaxHeight) .. ",")
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

main(100)

-- print("The tower of rocks is " .. chamber.maxHeight .. " units tall")

local sequence = { 1, 3, 2, 1, 2, 1, 3, 2, 2, 0, 1, 3, 2, 0, 2, 1, 3, 3, 4, 0, 1, 2, 3, 0, 1, 1, 3, 2, 2, 0, 0, 2, 3, 4,
    0, 1, 2, 1, 2, 0, 1, 2, 1, 2, 0, 1, 3, 2, 0, 0, 1, 3, 3, 4, 0, 1, 2, 3, 0, 1, 1, 3, 2, 2, 0, 0, 2, 3, 4, 0, 1, 2, 1,
    2, 0, 1, 2, 1, 2, 0, 1, 3, 2, 0, 0, 1, 3, 3, 4, 0, 1, 2, 3, 0, 1, 1, 3, 2, 2, 0 }

    --sequence = {"a","c","b","d","f","c","b","d","f","c","b"}
local dict = {}

--Checks for the largest common prefix
local function lcp(s, t)
    local n = math.min(#s, #t)
    for i = 1, n do
        if s[i] ~= t[i] then
            return { table.unpack(s, 1, i) }
        end
    end
    return { table.unpack(s, 1, n) }
end

local lrs = {}
local n = #sequence
for i = 1, n do
    for j = i + 1, n do
        local x = lcp({ table.unpack(sequence, i, n) }, { table.unpack(sequence, j, n) })
        if #x > #lrs then
            print(table.concat(x), i, j)
            if #x + i > j then return end
            lrs = x
        end
    end
end
print("Longest repeating sequence: ", table.concat(lrs, ""));
