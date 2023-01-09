local allCommands = {}
local timeLine = { x = 1 }
local crt = {} -- 240 values

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

local function printCrt()
    for _, v in ipairs(crt) do
        io.write(v)
        if _ % 40 == 0 then print() end
    end
    print()
end

local function applyCycle(n, v)
    print("applyCycle. n=" .. n .. ", v=" .. v .. ", mask=[" .. v - 1 .. "-" .. v + 1 .. "]")
    if (n - 1)%40 >= v - 1 and (n - 1)%40 <= v + 1 then
        crt[n] = "#"
    else
        crt[n] = "."
    end
    printCrt()
end

for n, v in ipairs(timeLine) do
    applyCycle(n, v)
end

