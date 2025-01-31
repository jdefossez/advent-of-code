local cubes = {}

for line in io.lines("./data/input_18.txt") do
    line = "{" .. line .. "}"
    local cube = load("return " .. line)()
    cubes[#cubes + 1] = cube
end

local function distanceManhattan3D(cubeA, cubeB)
    return math.abs(cubeA[1] - cubeB[1]) + math.abs(cubeA[2] - cubeB[2]) + math.abs(cubeA[3] - cubeB[3])
end

local count = 0
for i = 1, #cubes do
    for j = i + 1, #cubes do
        local distance = distanceManhattan3D(cubes[i], cubes[j])
        if distance == 1 then
            count = count + 2
        end
    end
end

print("The surface area is ", #cubes * 6 - count)
