local function check(line)
    for c in line:gmatch"." do
        if select(2, line:gsub(c, "")) > 1 then return false end
    end
    return true
end

local f = io.open("./data/input_06.txt") -- 'r' is unnecessary because it's a default value.
local line = ""

if f ~= nil then
    line = f:read()
    f:close()
end

--line = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"

local subLine
local indexMarker
for i = 1, string.len(line)-14 do
    subLine = string.sub(line, i, i+13)
    if check(subLine) then 
        indexMarker = i+13
        break
    end
end

print(indexMarker)