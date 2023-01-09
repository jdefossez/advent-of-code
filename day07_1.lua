local function printDir(dir, level)
    if dir then
        local prefix = string.rep(" ", level)
        print(prefix .. dir.name)
        for _, child in ipairs(dir.children) do
            printDir(child, level + 2)
        end
        for _, file in ipairs(dir.files) do
            print(prefix .. "  " .. file.fileName, file.size)
        end
    end
end

local function computeSize(dir)
    local size = 0
    for _, file in ipairs(dir.files) do
        size = size + file.size
    end
    for _, child in ipairs(dir.children) do
        size = size + computeSize(child)
    end
    dir.size = size

    print("Size of " .. dir.fullName .. " is " .. dir.size)
    return size
end

local allLines = {}
local i = 1
for line in io.lines("./data/input_07.txt") do allLines[i] = line
i = i + 1

end
local allDirByFullName = {}
local currentDirName = ""
local currentDir = nil
local newDir = nil
for _, line in ipairs(allLines) do
    --print("current line: ", line)
    if string.find(line, "$ cd") == 1 then
        currentDirName = string.sub(line, 6)
        -- si l'instruction ne demande pas de remonter dans le repertoire parent
        if currentDirName ~= ".." then
            -- connait-on déjà ce repertoire ?
            local key
            if not currentDir then
                key = currentDirName
            elseif currentDir.fullName == "/" then
                key = "/" .. currentDirName
            else
                key = currentDir.fullName .. "/" .. currentDirName
            end
            if allDirByFullName[key] == nil
            then
                -- on ne le connait pas encore
                -- on le crée
                -- print("create a new dir. currentDirName: ", currentDirName, "fullName: ", key)
                newDir = { name = currentDirName, fullName = key, size = -1, files = {}, children = {}, parent = currentDir }
                -- on l'ajoute à la liste des fils de son père (si son père existe)
                if currentDir then
                    table.insert(currentDir.children, newDir)
                end
                -- on l'ajoute à la map de tous les repertoires par fullName
                allDirByFullName[key] = newDir
            end
            -- on sauvegarde le fait que le repertoire courant soit le repertoire dont la clé est key
            currentDir = allDirByFullName[key]
        else
            -- si l'instruction demande de remonter dans le repertoire parent
            --print("currentDir: ", currentDir.name)
            currentDir = currentDir.parent
            -- print("currentDir: ", currentDir.name)
        end
        --elseif string.find(line, "$ ls") == 1 then
        -- on se met en mode lecture du repertoire courant. Rien à faire ?
        --elseif string.find(line, "dir ") == 1 then
        -- on ne fait rien ?
    elseif string.find(line, "%d+") == 1 then
        local _, _, size = line:find("(%d+)")
        local _, _, fileName = line:find("([%a%.]+)")
        -- print("size: ", size, "fileName: ")
        table.insert(currentDir.files, { fileName = fileName, size = size })
        -- ajout du fichier au repertoire courant
    end
end


local root = allDirByFullName["/"]
printDir(root, 0)
print()

printDir(allDirByFullName["/a"], 0)

computeSize(root)

local totalSize = 0

for _, dir in pairs(allDirByFullName) do
    if dir.size < 100000 then
        totalSize = totalSize + dir.size
    end
end

print("totalSize:", totalSize)