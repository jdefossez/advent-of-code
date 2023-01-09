local sensors = {}

for line in io.lines("./data/input_15.txt") do
    line = string.gsub(line, "Sensor at ", "sensor={")
        :gsub(": closest beacon is at ", "}, beacon={")
        :gsub("[\n\r]", "")
    line = "{" .. line .. "}}"
    print(line)
    local data = load("return " .. line)()
    local distance = math.abs(data.beacon.x - data.sensor.x) + math.abs(data.beacon.y - data.sensor.y)
    data["distance"] = distance

    sensors[#sensors + 1] = data
    print("Sensor {" .. data.sensor.x .. ", " .. data.sensor.y .. "} ", "distance", data.distance)
end

local function computeRanges(yIndex)
    local ranges = {}
    for _, d in ipairs(sensors) do -- loop over each sensor data
        local distance = math.abs(d.sensor.y - yIndex)
        if distance < d.distance then -- if line is in the range of sensor covered distance
            local indexMin = d.sensor.x - d.distance + distance
            local indexMax = d.sensor.x + d.distance - distance
            ranges[#ranges + 1] = { indexMin, indexMax }
        end
    end
    return ranges
end

local function merge_range(range, ranges)
    for i, other_range in ipairs(ranges) do
        -- my self
        if range[1] == other_range[1] and range[2] == other_range[2] then
        elseif range[2] >= other_range[1] and range[2] <= other_range[2] then
            range[2] = other_range[2]
            table.remove(ranges, i)
            return merge_range(range, ranges)
        elseif range[1] <= other_range[1] and range[2] >= other_range[2] then
            table.remove(ranges, i)
            return merge_range(range, ranges)
        end

    end
end

local function merge_ranges(ranges)
    table.sort(ranges, function(a, b)
        return a[1] < b[1]
    end)
    for _, range in ipairs(ranges) do
        merge_range(range, ranges)
    end
end

--[[
    Algo :
    - for each line in all potential lines (1, size)
        - for each sensor
            - compute ranges
            - merge ranges
            - if nb ranges > 1 => check next line
            - if nb ranges <= 1
                - check min and max of the range
                    - if equal to 1 and size => check next line
                    - if not equal => this is the line we're looking for
]] --
local function main(size)
    for y = 0, size - 1 do
        print("trying line:", y)
        local ranges = computeRanges(y)
        merge_ranges(ranges)

        local possibles = {}
        for i, range in ipairs(ranges) do
            local next_range = ranges[i + 1]
            if next_range ~= nil then
                table.insert(possibles, { range[2], next_range[1] })
            end
        end
        for i, possible_range in ipairs(possibles) do
            for x = possible_range[1] + 1, possible_range[2] - 1 do
                print('beacon', x, y)
                print('tuning frequency', x * 4000000 + y)
                return
            end
        end

    end
end

main(4000000)