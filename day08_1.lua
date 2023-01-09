local allLines = {}
local i = 1
for line in io.lines("./data/input_08.txt") do allLines[i] = line
    i = i + 1
end

local count = 2 * #allLines[1] + 2 * (#allLines - 2)

local allHeights = {}
for row = 1, #allLines do
    allHeights[row] = {}
    for col = 1, #allLines[1] do
        local height = tonumber(allLines[row]:sub(col, col))
        allHeights[row][col] = height
    end
end

for row = 1, #allHeights do
    for col = 1, #allHeights[1] do
        io.write(allHeights[row][col])
    end
    print()
end

for row = 2, #allHeights - 1 do
    for col = 2, #allHeights[1] - 1 do
        local height = allHeights[row][col]
        local visible = true
        -- test on the left
        print("hauteur  courante", height)
        for c = 1, col - 1 do if allHeights[row][c] >= height then visible = false end end
        print("test left", visible)
        if visible then
            count = count + 1
        end
        if not visible then
            -- test on the right
            visible = true
            for c = col + 1, #allHeights[row] do if allHeights[row][c] >= height then visible = false end end
            if visible then
                count = count + 1
            end
            print("test right", visible)
        end
        if not visible then
            -- test on the up
            visible = true
            for r = 1, row - 1 do if allHeights[r][col] >= height then visible = false end end
            if visible then
                count = count + 1
            end
            print("test up", visible)
        end
        if not visible then
            -- test on the bottom
            visible = true
            for r = row + 1, #allHeights do if allHeights[r][col] >= height then visible = false end end
            if visible then
                count = count + 1
            end
            print("test bottom", visible)
        end
    end
end

print("Number of visible trees:", count)
