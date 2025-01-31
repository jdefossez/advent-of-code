local a, b, c = 0, 0, 0
local sum = 0

function min(a, b, c, max)
    if a <= b and a <= c and a < max then return max, b, c end
    if b <= a and b <= c and b < max then return a, max, c end
    if c < max then return a, b, max end
    return a, b, c
end

print("******************************")

for line in io.lines ("./data/input_01.txt") do
    --print(a, b, c, sum)
    if line ~= "" then
        sum = sum + tonumber(line)
    else
        sum = 0
    end
    a, b, c = min(a, b, c, sum)
end


print(a + b + c)
