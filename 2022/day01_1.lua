local max = 0
local sum = 0
for line in io.lines ("./data/input_01.txt") do
    if line ~= "" then
        sum = sum + tonumber(line)
    else
        sum = 0
    end
    if sum > max then max = sum end
end

print(max)
