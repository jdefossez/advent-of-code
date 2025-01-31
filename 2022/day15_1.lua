local data = {}

local xMin, xMax, yMin, yMax = 999999999, 0, 999999999, 0

for line in io.lines("./data/input_15.txt") do
    line = string.gsub(line, "Sensor at ", "sensor={")
        :gsub(": closest beacon is at ", "}, beacon={")
        :gsub("[\n\r]", "")
    line = "{" .. line .. "}}"
    print(line)
    data[#data + 1] = load("return " .. line)()
    local distance = math.abs(data[#data].beacon.x - data[#data].sensor.x)
        + math.abs(data[#data].beacon.y - data[#data].sensor.y)
    data[#data]["distance"] = distance

    if data[#data].sensor.x - distance < xMin then xMin = data[#data].sensor.x - distance end
    if data[#data].sensor.x + distance > xMax then xMax = data[#data].sensor.x + distance end

    if data[#data].sensor.y - distance < yMin then yMin = data[#data].sensor.y - distance end
    if data[#data].sensor.y + distance > yMax then yMax = data[#data].sensor.y + distance end

    --print("distance", data[#data]["distance"], "xMin", xMin, "xMax", xMax, "yMin", yMin, "yMax", yMax)
end

local lineToCheck = {}
for i = xMin, xMax do
    lineToCheck[i] = "."
end


local function computeSensor(d, yIndex)
    local indexMin = d.sensor.x - d.distance + math.abs(d.sensor.y - yIndex)
    local indexMax = d.sensor.x + d.distance - math.abs(d.sensor.y - yIndex)
    for i = indexMin, indexMax do
        lineToCheck[i] = "#"
    end
    --print("Sensor {" .. d.sensor.x .. ", " .. d.sensor.y .. "} ", "indexMin", indexMin, "indexMax", indexMax)
    --for i = xMin, xMax do
    --    io.write(lineToCheck[i])
    --end

    --print()
end

local count = 0
local function computeImpossiblePositions(yIndex)
    for _, d in ipairs(data) do
        --print("Sensor {" .. d.sensor.x .. ", " .. d.sensor.y .. "} ")
        if d.distance >= math.abs(d.sensor.y - yIndex) then
            computeSensor(d, yIndex)
        end
    end
    for _, d in ipairs(data) do
        if d.beacon.y == yIndex then
            lineToCheck[d.beacon.x] = "B"
        end
    end
    for i = xMin, xMax do
        if lineToCheck[i] == "#" then count = count + 1 end
        --io.write(lineToCheck[i])
    end
    --print()
end

computeImpossiblePositions(2000000)

print("Number of impossible locations ", count)
