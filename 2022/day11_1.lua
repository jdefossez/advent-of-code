local allMonkeys = {}
local allMonkeysSorted = {}

local function readMonkeyNumber(line)
    local _, _, monkeyNumber = line:find("Monkey (%d+):")
    if monkeyNumber then
        return monkeyNumber
    else
        return nil
    end
end

local function readitems(line)
    if line:find("Starting items:") then
        allMonkeysSorted[#allMonkeysSorted].items = {}
        for w in line:gmatch("(%d+)") do
            table.insert(allMonkeysSorted[#allMonkeysSorted].items, w)
        end
    end
end

local function readOperation(line)
    if line:find("Operation:") then
        -- print("line: " .. line, "Operation: found")
        local _, _, funcStr = line:find("Operation: (.+)")
        local func, err = load("return function(old) " .. funcStr .. " return new end")
        if func then
            local ok, operation = pcall(func)
            if ok then
                allMonkeysSorted[#allMonkeysSorted].operation = operation
            end
        end
    end
end

local function readTest(line)
    if line:find("Test: divisible by") then
        local _, _, divisor = line:find("Test: divisible by (%d+)")
        allMonkeysSorted[#allMonkeysSorted].divisor = divisor
    end
end

local function readMonkeyNumberWhenTrue(line)
    if line:find("If true: throw to monkey") then
        local _, _, monkeyNumber = line:find("If true: throw to monkey (%d+)")
        allMonkeysSorted[#allMonkeysSorted].monkeyNumberTrue = monkeyNumber
    end
end

local function readMonkeyNumberWhenFalse(line)
    if line:find("If false: throw to monkey") then
        local _, _, monkeyNumber = line:find("If false: throw to monkey (%d+)")
        allMonkeysSorted[#allMonkeysSorted].monkeyNumberFalse = monkeyNumber
    end
end

for line in io.lines("./data/input_11.txt") do
    local crtMonkeyNumber = readMonkeyNumber(line)
    if crtMonkeyNumber then
        allMonkeys[crtMonkeyNumber] = { number = crtMonkeyNumber, itemsInspected = 0 }
        allMonkeysSorted[#allMonkeysSorted + 1] = allMonkeys[crtMonkeyNumber]
    end
    readitems(line)
    readOperation(line)
    readTest(line)
    readMonkeyNumberWhenTrue(line)
    readMonkeyNumberWhenFalse(line)
end

local function printAllMonkeysSorted()
    print("All monkeys sorted: ")
    for num, monkey in ipairs(allMonkeysSorted) do
        -- monkey = allMonkeys[monkey.number]
        print("monkey " .. monkey.number)
        for _, v in ipairs(monkey.items) do
            io.write(v .. ",")
        end
        print()
        --print("divisor:", monkey.divisor)
        --print(monkey.monkeyNumberTrue)
        --print(monkey.monkeyNumberFalse)
        print("Number of inspected items:", monkey.itemsInspected)
    end
end

local function simulateMonkey(monkey)
    for _, item in ipairs(monkey.items) do
        monkey.itemsInspected = monkey.itemsInspected + 1
        monkey.items[_] = nil
        local currentItem = math.floor(monkey.operation(item) / 3)
        if currentItem % monkey.divisor == 0 then
            table.insert(allMonkeys[monkey.monkeyNumberTrue].items, currentItem)
        else
            table.insert(allMonkeys[monkey.monkeyNumberFalse].items, currentItem)
        end
    end
end

local function simulate()
    for i = 1, 20 do -- 20 rounds
        for _, monkey in ipairs(allMonkeysSorted) do
            simulateMonkey(monkey)
        end
    end
    return table.sort(allMonkeysSorted, function(a, b) return a.itemsInspected > b.itemsInspected end)
end

simulate()

printAllMonkeysSorted()
for _, monkey in ipairs(allMonkeysSorted) do
    print("Monkey " .. monkey.number .. " has inspected " .. monkey.itemsInspected .. " items")
end

print("Monkey buisiness is", allMonkeysSorted[1].itemsInspected * allMonkeysSorted[2].itemsInspected)
