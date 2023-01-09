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

local totalCount = 0
for row = 2, #allHeights - 1 do
    for col = 2, #allHeights[1] - 1 do

        local height = allHeights[row][col]

        -- test on the left
        local leftCount = 0
        for c = col - 1, 1, -1 do
            leftCount = leftCount + 1
            if allHeights[row][c] >= height then
                break
            end
        end

        -- test on the right
        local rightCount = 0
        for c = col + 1, #allHeights[row] do
            rightCount = rightCount + 1
            if allHeights[row][c] >= height then
                break
            end
        end

        -- test on the up
        local upCount = 0
        for r = row - 1, 1, -1 do
            upCount = upCount + 1
            if allHeights[r][col] >= height then
                break
            end
        end

        -- test on down
        local downCount = 0
        for r = row + 1, #allHeights do
            downCount = downCount + 1
            if allHeights[r][col] >= height then
                break
            end
        end

        if leftCount * upCount * rightCount * downCount > totalCount then
            totalCount = leftCount * upCount * rightCount * downCount
        end
    end
end

print("Highest score:", totalCount)
