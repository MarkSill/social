local world = script:getParent()
world.gravX, world.gravY = 0, 0
local player = game:findChild("Players"):children()[1]

local block = Instance:create("rectangle", world)
block.position = Vector2.new(10, 10)

local left, right, up, down = false, false, false, false

player:addKeyboardDownCallback(function(key, char)
	if key == Keyboard.KEY_LEFT then
		left = true
	elseif key == Keyboard.KEY_RIGHT then
		right = true
	elseif key == Keyboard.KEY_UP then
		up = true
	elseif key == Keyboard.KEY_DOWN then
		down = true
	end
end)
player:addKeyboardUpCallback(function(key, char)
	if key == Keyboard.KEY_LEFT then
		left = false
	elseif key == Keyboard.KEY_RIGHT then
		right = false
	elseif key == Keyboard.KEY_UP then
		up = false
	elseif key == Keyboard.KEY_DOWN then
		down = false
	end
end)

while true do
	wait(5)
	local speed = 5
	local x, y = 0, 0
	if left then
		x = x - speed
	end
	if right then
		x = x + speed
	end
	if up then
		y = y + speed
	end
	if down then
		y = y - speed
	end
	block:getBody():applyForce(Vector2.new(x, y))
end
