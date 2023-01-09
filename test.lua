-- Returns the longest repeating non-overlapping
-- substring in str
local function longestRepeatedSubstring(str)
    local n = #str
    local LCSRe = {} --new int[n + 1][n + 1];

    local res = "" -- To store result
    local res_length = 0 -- To store length of result

    -- building table in bottom-up manner
    local index = 0
    for i = 1, n do
        for j = i + 1, n do
            -- (j-i) > LCSRe[i-1][j-1] to remove
            -- overlapping
            if (str:sub(i - 1, i - 1) == str:sub(j - 1, j - 1)
                and LCSRe[i - 1][j - 1] < (j - i)) then
                LCSRe[i][j] = LCSRe[i - 1][j - 1] + 1;

                -- updating maximum length of the
                -- substring and updating the finishing
                -- index of the suffix
                if (LCSRe[i][j] > res_length) then
                    res_length = LCSRe[i][j];
                    index = math.max(i, index);
                end
            else
                LCSRe[i][j] = 0;
            end
        end
    end

    -- If we have non-empty result, then insert all
    -- characters from first character to last
    -- character of String
    if (res_length > 0) then
        for i = index - res_length + 1, index do
            res = res .. str:sub(i - 1, i - 1)
        end
    end

    return res;
end

-- Driver program to test the above function

local str = "geeksforgeeks"
print(longestRepeatedSubstring(str))
