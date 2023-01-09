local allMoves = {}

for line in io.lines("./data/input_09.txt") do
    local _, _, count = line:find("(%d+)")
    local _, _, dir = line:find("(%a)")
    for i = 1, count do
        table.insert(allMoves, dir)
    end
end

local rope = {}
for i = 1, 10 do
    rope[i] = { x = 1, y = 1 }
end

local function printRope()
    for _, knot in ipairs(rope) do
        io.write(knot.x .. "," .. knot.y .. " ")
    end
    print()
end

local function moveLeft(a)
    a.x = a.x - 1
end

local function moveRight(a)
    a.x = a.x + 1
end

local function moveUp(a)
    a.y = a.y + 1
end

local function moveDown(a)
    a.y = a.y - 1
end

local function moveUpLeft(a)
    a.x = a.x - 1
    a.y = a.y + 1
end

local function moveUpRight(a)
    a.x = a.x + 1
    a.y = a.y + 1
end

local function moveDownRight(a)
    a.x = a.x + 1
    a.y = a.y - 1
end

local function moveDownLeft(a)
    a.x = a.x - 1
    a.y = a.y - 1
end

local function computeNextMove(a, b)
    if a.y == b.y + 2 then -- haut
        if a.x > b.x then -- right
            return "UR"
        elseif a.x < b.x then -- left
            return "UL"
        else
            return "U"
        end
    end
    if a.y == b.y - 2 then -- bas
        if a.x > b.x then -- right
            return "DR"
        elseif a.x < b.x then -- left
            return "DL"
        else
            return "D"
        end
    end
    if a.x == b.x - 2 then -- left
        if a.y < b.y then -- down
            return "DL"
        elseif a.y > b.y then -- up
            return "UL"
        else
            return "L"
        end
    end
    if a.x == b.x + 2 then -- left
        if a.y < b.y then -- down
            return "DR"
        elseif a.y > b.y then -- up
            return "UR"
        else
            return "R"
        end
    end
    return nil
end

local actions = {
    L = moveLeft,
    UL = moveUpLeft,
    U = moveUp,
    UR = moveUpRight,
    R = moveRight,
    DR = moveDownRight,
    D = moveDown,
    DL = moveDownLeft
}

local visited = { ["1;1"] = true }

local function applyMove(move)
    local nextMove = move
    --local applyA = true
    -- print("applyMove", nextMove)

    for i = 1, #rope - 1 do
        actions[nextMove](rope[i])
        nextMove = computeNextMove(rope[i], rope[i + 1])
        if not nextMove then break end
        --printRope()
    end
    if nextMove then actions[nextMove](rope[#rope]) end
    visited[rope[#rope].x .. ";" .. rope[#rope].y] = true
end

printRope()

--[[
print("************************************************************")
applyMove("R")
printRope()
print("************************************************************")
applyMove("R")
printRope()
print("************************************************************")
applyMove("R")
printRope()
print("************************************************************")
applyMove("R")
printRope()
print("************************************************************")
applyMove("U")
printRope()
print("************************************************************")
applyMove("U")
printRope()
--]]

---[[
for _, move in ipairs(allMoves) do
    applyMove(move)
    -- printRope()
end
--]]

printRope()

local function printVisited()
    for k, _ in pairs(visited) do
        print(k)
    end
end

-- printVisited()

local count = 0
for k, _ in pairs(visited) do
    count = count + 1
end
print("Number of visited locations: ", count)
