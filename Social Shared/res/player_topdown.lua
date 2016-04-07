local player = script:getParent()
local world = game:findChild("World")
world.gravX, world.gravY = 0, 0
local x, y = 0, 0

local block = Instance:create("rectangle", world)
block.rotationLocked = true
block.position = Vector2.new(10, 10)
block:getBody():setLinearDamping(1)

local keyboard = false

player:addControllerCallback(function(controller, name, value)
	if keyboard then return end
	if name == Controllers.LEFT_X then
		x = value;
	elseif name == Controllers.LEFT_Y then
		y = value;
	end
end)

player:addKeyboardDownCallback(function(key, char)
	if key == Keyboard.KEY_RIGHT then
		x = 1
	elseif key == Keyboard.KEY_LEFT then
		x = -1
	elseif key == Keyboard.KEY_UP then
		y = 1
	elseif key == Keyboard.KEY_DOWN then
		y = -1
	end
	if x ~= 0 or y ~= 0 then
		keyboard = true
	end
end)

player:addKeyboardUpCallback(function(key, char)
	if key == Keyboard.KEY_RIGHT and x == 1 then
		x = 0
	elseif key == Keyboard.KEY_LEFT and x == -1 then
		x = 0
	elseif key == Keyboard.KEY_UP and y == 1 then
		y = 0
	elseif key == Keyboard.KEY_DOWN and y == -1 then
		y = 0
	end
	if x == 0 and y == 0 then
		keyboard = false
	end
end)

while true do
	wait(1000/60)
	local body = block:getBody()
	body:applyForce(Vector2.new(x * 10, y * 10))
end
