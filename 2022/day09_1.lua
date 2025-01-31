local allMoves = {}

for line in io.lines("./data/test_09.txt") do
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

local function moveHorizontally(a, b)
    if b.y == a.y + 1 then -- Top
        b.y = b.y - 1
        return "D"
    elseif b.y == a.y - 1 then -- down
        b.y = b.y + 1
        return "U"
    end
    return ""
end

local function moveVertically(a, b)
    if b.x == a.x - 1 then -- Left
        b.x = b.x + 1
        return "R"
    elseif b.x == a.x + 1 then -- Right
        b.x = b.x - 1
        return "L"
    end
    return ""
end

-- ces fonctions doivent retourner le nom du mouvement calculé de b

local function moveLeft(a, b, applyA)
    print("moveLeft")
    local nextMove = ""
    if b.x == a.x + 1 then -- b is on the right
        b.x = b.x - 1
        nextMove = moveHorizontally(a, b)
        nextMove = "L" .. nextMove
    end
    if applyA then a.x = b.x - 1 end
    return nextMove
end

local function moveRight(a, b, applyA)
    print("moveRight. a=(" .. a.x .. "," .. a.y .. "), b=(" .. b.x .. "," .. b.y .. "), applyA=" .. tostring(applyA))
    local nextMove = ""
    if b.x == a.x - 1 then -- b is on the left
        b.x = b.x + 1
        nextMove = moveHorizontally(a, b)
        nextMove = "R" .. nextMove
    end
    if applyA then a.x = a.x + 1 end
    return nextMove
end

local function moveUp(a, b, applyA)
    print("moveUp")
    local nextMove = ""
    if b.y == a.y - 1 then -- Tail is on the down
        b.y = b.y + 1
        nextMove = moveVertically()
        nextMove = "U" .. nextMove
    end
    if applyA then a.y = a.y + 1 end
    return nextMove
end

local function moveDown(a, b, applyA)
    print("moveDown")
    local nextMove = ""
    if b.y == a.y + 1 then -- Tail is on the up
        b.y = b.y - 1
        nextMove = moveVertically()
        nextMove = "D" .. nextMove
    end
    if applyA then a.y = a.y - 1 end
    return nextMove
end

local function moveUpLeft(a, b, applyA)
    print("moveUpLeft")
    local nextMove = ""
    if b.x == a.x - 1 and b.y == a.y - 1 then
        -- b was on left down
        b.y = b.y + 1
        nextMove = "U"
    elseif b.y == a.y - 1 and b.x == a.x then
        -- b was down
        b.y = b.y + 1
        nextMove = "U"
    elseif b.y == a.y - 1 and b.x == a.x + 1 then
        -- b was right down
        b.y = b.y + 1
        b.x = b.x - 1
        nextMove = "UL"
    elseif b.y == a.y and b.x == a.x + 1 then
        -- b was on the right
        b.x = b.x - 1
        nextMove = "L"
    elseif b.y == a.y + 1 and b.x == a.x + 1 then
        -- b was on the top right
        b.x = b.x - 1
        nextMove = "L"
    end
    if applyA then
        a.x = a.x - 1
        a.y = a.y + 1
    end
    return nextMove
end

local function moveUpRight(a, b, applyA)
    print("moveUpRight")
    local nextMove = ""
    if b.x == a.x - 1 and b.y == a.y + 1 then
        -- b was on top left
        b.x = b.x + 1
        nextMove = "R"
    elseif b.y == a.y and b.x == a.x - 1 then
        -- b was left
        b.x = b.x + 1
        nextMove = "R"
    elseif b.y == a.y - 1 and b.x == a.x - 1 then
        -- b was left down
        b.y = b.y + 1
        b.x = b.x + 1
        nextMove = "UR"
    elseif b.y == a.y - 1 and b.x == a.x then
        -- b was down
        b.y = b.y + 1
        nextMove = "U"
    elseif b.y == a.y - 1 and b.x == a.x + 1 then
        -- b was on the down right
        b.y = b.y + 1
        nextMove = "U"
    end
    if applyA then
        a.x = a.x + 1
        a.y = a.y + 1
    end
    return nextMove
end

local function moveDownRight(a, b, applyA)
    print("moveDownRight")
    local nextMove = ""
    if b.x == a.x - 1 and b.y == a.y - 1 then
        -- b was on down left
        b.x = b.x + 1
        nextMove = "R"
    elseif b.y == a.y and b.x == a.x - 1 then
        -- b was left
        b.x = b.x + 1
        nextMove = "R"
    elseif b.y == a.y + 1 and b.x == a.x - 1 then
        -- b was top left
        b.y = b.y - 1
        b.x = b.x + 1
        nextMove = "DR"
    elseif b.y == a.y + 1 and b.x == a.x then
        -- b was on top
        b.y = b.y - 1
        nextMove = "D"
    elseif b.y == a.y + 1 and b.x == a.x + 1 then
        -- b was on the top right
        b.y = b.y - 1
        nextMove = "D"
    end
    if applyA then
        a.x = a.x + 1
        a.y = a.y - 1
    end
    return nextMove
end

local function moveDownLeft(a, b, applyA)
    print("moveDownLeft")
    local nextMove = ""
    if b.x == a.x - 1 and b.y == a.y + 1 then
        -- b was on top left
        b.y = b.y - 1
        nextMove = "D"
    elseif b.y == a.y + 1 and b.x == a.x then
        -- b was top
        b.y = b.y - 1
        nextMove = "D"
    elseif b.y == a.y + 1 and b.x == a.x + 1 then
        -- b was top right
        b.y = b.y - 1
        b.x = b.x - 1
        nextMove = "DL"
    elseif b.y == a.y and b.x == a.x + 1 then
        -- b was on right
        b.x = b.x - 1
        nextMove = "L"
    elseif b.y == a.y - 1 and b.x == a.x + 1 then
        -- b was on the bottom right
        b.x = b.x - 1
        nextMove = "L"
    end
    if applyA then
        a.x = a.x - 1
        a.y = a.y - 1
    end
    return nextMove
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

local visited = {}
visited["1;1"] = true

local function applyMove(move)
    local nextMove = move
    local applyA = true
    print("applyMove", move)
    for i = 1, #rope - 1 do
        if nextMove == "" then break end -- If the following knot has not moved, break
        nextMove = actions[nextMove](rope[i], rope[i + 1], applyA)
        applyA = false
        -- ici nextmove a déjà été appliqué ! Gredin ! Ne pas tenter de l'appliquer une deuxième fois
        printRope()
        print("nextMove", nextMove)
    end
    visited[rope[10].x .. ";" .. rope[10].y] = true
end

printRope()
print("************************************************************")
applyMove("R")
printRope()
print("************************************************************")
applyMove("R")
printRope()

--[[
for _, move in ipairs(allMoves) do
    applyMove(move)
end
--]]

local function printVisited()
    for k, _ in pairs(visited) do
        print(k)
    end
end

printVisited()
local count = 0
for k, _ in pairs(visited) do
    count = count + 1
end
print("Number of visited locations: ", count)
