local co;

function start(code)
	co = coroutine.create(function()
		load(code)()
	end)
	load(code)()
end

function stop()
	kill()
	print("Hello!")
end