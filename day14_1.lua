local paths = {}
local xMin, xMax, yMin, yMax = 999999999, 1, 0, 1
local grid

for line in io.lines("./data/input_14.txt") do
    line = string.gsub(line, " %-> ", "}, {")
        :gsub("[\n\r]", "")
    line = "{" .. line .. "}"
    line = line:gsub("{", "{x=")
        :gsub(",", ", y=")
        :gsub("y= ", "")
    --print(line)
    paths[#paths + 1] = load("return {" .. line .. "}")()
    for _, c in ipairs(paths[#paths]) do
        --print(c.x, c.y)
        if c.x < xMin then xMin = c.x elseif c.x > xMax then xMax = c.x end
        if c.y > yMax then yMax = c.y end
    end
end

local function initGrid()
    local grid = {}
    local emptyRow = {}

    for r = yMin, yMax do
        grid[r] = {}
        for c = xMin, xMax do
            --print("c", c)
            grid[r][c] = "."
        end
    end
    return grid
end

local function printGrid()
    for i = 0, #grid do
        local row = grid[i]
        for col = xMin, xMax do
            --print(col)
            io.write(row[col])
        end
        print()
    end
    print()
end

local function fillLine(p1, p2)
    --print("fillLine p1(" .. p1.x .. "," .. p1.y .. ") p2(" .. p2.x .. "," .. p2.y .. ")")

    if p1.y == p2.y then -- if same row
        if p1.x < p2.x then -- p1 is lefter
            --print("cas 1")
            for i = p1.x, p2.x do
                grid[p1.y][i] = "#"
            end
        else -- p2 is lefter
            --print("cas 2")
            for i = p2.x, p1.x do
                grid[p1.y][i] = "#"
            end
        end
    else -- if same col
        if p1.y < p2.y then -- p1 is higher
            --print("cas 3")
            for i = p1.y, p2.y do
                grid[i][p1.x] = "#"
            end
        else -- p2 is higher
            --print("cas 4")
            for i = p2.y, p1.y do
                grid[i][p1.x] = "#"
            end
        end
    end
    -- printGrid()
end

local function placeStructures(structures)

    for _, structure in ipairs(structures) do
        local previousPoint = structure[1]
        local currentPoint = structure[2]

        local index = 2
        repeat
            fillLine(previousPoint, currentPoint)
            previousPoint = currentPoint
            index = index + 1
            currentPoint = structure[index]
        until index > #structure
    end
end

local function moveGrain(start)
    --print(start.x, start.y)
    if grid[start.y + 1][start.x] == nil then -- Down is out of the grid
        return 0
    elseif grid[start.y + 1][start.x] == "." then -- Down is an free place
        return moveGrain({ x = start.x, y = start.y + 1 })
    end

    if grid[start.y + 1][start.x - 1] == nil then -- down left is out of the grid
        return 0
    elseif grid[start.y + 1][start.x - 1] == "." then -- down left is an free place
        return moveGrain({ x = start.x - 1, y = start.y + 1 })
    end

    if grid[start.y + 1][start.x + 1] == nil then -- down right is out of the grid
        return 0
    elseif grid[start.y + 1][start.x + 1] == "." then -- down right is a free place
        return moveGrain({ x = start.x + 1, y = start.y + 1 })
    end

    grid[start.y][start.x] = "o"
    return 1
end

grid = initGrid()
placeStructures(paths)
printGrid()
local totalUnits = 0
while (true) do
    local result = moveGrain({ x = 500, y = 0 })
    if result == 0 then break end
    totalUnits = totalUnits + result
    printGrid()
end

print("Total units: ", totalUnits)
