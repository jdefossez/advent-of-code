local allCommands = {}
local timeLine = { x = 1 }

for line in io.lines("./data/input_10.txt") do
    local _, _, command = line:find("(%a+)")
    local _, _, amount = line:find("(%-?%d+)")
    allCommands[#allCommands + 1] = { command = command, amount = amount }
end

local function printTimeLine()
    io.write("TimeLine: ")
    for _, v in ipairs(timeLine) do
        io.write(v .. ",")
    end
    print(" x:" .. timeLine.x)
end

local function applyAddx(amount)
    timeLine[#timeLine + 1] = timeLine.x
    timeLine[#timeLine + 1] = timeLine.x
    timeLine.x = amount + timeLine.x
end

local function applyNoop()
    timeLine[#timeLine + 1] = timeLine.x
end

-- printTimeLine()

---[[
for _, c in ipairs(allCommands) do
    if c.command == "noop" then
        applyNoop()
    elseif c.command == "addx" then
        applyAddx(c.amount)
    end
end
--]]

--printTimeLine()

local sum = 20*timeLine[20] + 60*timeLine[60] + 100*timeLine[100] + 140*timeLine[140] + 180*timeLine[180] + 220*timeLine[220]

print("Sum of six signal strengths is:", sum)