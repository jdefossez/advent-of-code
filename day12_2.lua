List = require("list")

local allNodes = {}
local letters = 'abcdefghijklmnopqrstuvwxyz'

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

local y = 1
local E

for line in io.lines("./data/input_12.txt") do
    local row = {}
    for i = 1, #line do
        -- print("x:" .. i .. ",y:" .. y)
        row[i] = { x = i, y = y, char = line:sub(i, i), height = letters:find(line:sub(i, i)), neighbors = {},
            visited = false, level = 0 }
        if row[i].char == 'S' then
            row[i].height = 1
        elseif row[i].char == 'E' then
            row[i].height = 26
            E = row[i]
        end
    end
    allNodes[#allNodes + 1] = row
    y = y + 1
end

local function computeNeighbors(map, node, maxWidth, maxHeight)
    --print("computeNeighbors")
    -- printNode(node, false)
    -- left
    if node.x > 1 and map[node.y][node.x - 1].height >= node.height - 1 then
        node.neighbors[#node.neighbors + 1] = map[node.y][node.x - 1]
    end
    -- right
    if node.x < maxWidth and map[node.y][node.x + 1].height >= node.height - 1 then
        node.neighbors[#node.neighbors + 1] = map[node.y][node.x + 1]
    end
    --up
    if node.y > 1 and map[node.y - 1][node.x].height >= node.height - 1 then
        node.neighbors[#node.neighbors + 1] = map[node.y - 1][node.x]
    end
    --down
    if node.y < maxHeight and map[node.y + 1][node.x].height >= node.height - 1 then
        node.neighbors[#node.neighbors + 1] = map[node.y + 1][node.x]
    end
end

for _, row in ipairs(allNodes) do
    for _, node in ipairs(row) do
        computeNeighbors(allNodes, node, #allNodes[1], #allNodes)
    end
end

--printMap(allNodes)
--printNode(allNodes[1][1], false);

local function printListOfNodes(l)
    print("size de l:", #l)
    for i, n in ipairs(l) do
        print(i, n.char)
    end
end

local nbStep = 0
local function BFS(root)
    local f = List.new()
    List.pushRight(f, root)
    root.visited = true
    while (List.size(f) > 0) do
        root = List.popLeft(f);
        print("root", root.char, root.visited, root.level)
        if root.height == 1 then return root.level end
        if #root.neighbors > 0 then nbStep = nbStep + 1 end
        for _, n in ipairs(root.neighbors) do
            if not n.visited then
                List.pushRight(f, n)
                n.visited = true
                n.level = root.level + 1
            end
        end
        --printListOfNodes(f)
    end
end

-- trouver le premier (S)
-- trouver le dernier (E)
-- BFS(allNodes[1][1])
local nbSteps = BFS(E)

print("Number of steps until E is ", nbSteps)
