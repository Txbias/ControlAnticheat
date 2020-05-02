# ControlAnticheat
The all in one anticheat solution for minecraft servers
Warning: This plugin is still in beta. If you encounter a bug, bypass or false flag please report it.

## Compatibility:
CAC is currently only tested on the versions 1.8.8 and 1.12.2. If you test it with another version and it works please let me know.

## Feautures:
### Blocked Cheats:

Movement:
- Speed
- Waterwalk
- Flight
- Glide
- Blink
- Inventorymove
- Tower

Combat:
- Killaura (won't be prevented but registered)
- Fastbow
- Reach

Player:
- Ghosthand (open chests through walls)
- Nofall

### Commands:
- /cac info [player] - Shows which cheats were detected.
- /cac reload - Reloads the config.
- /cac notify - Toggles your notifications
- /ping [player] - Lets you see the ping of yourself or of another player

### Permissions:
- cac.* - Gives you all permissions other than cac.bypass
- cac.bypass - You wont be checked for cheats.
- cac.notify - You get notified when someone is cheating & lets you use /cac notify
- cac.info - Lets you use /cac info [player]
- cac.reload - Lets you use /cac reload
- cac.ping - Lets you use /ping [player]

### Config:
```
# The minimum time between two bow shots allowed. (in milliseconds)
minTimeBetweenBowShots: 149

# The maximum amount of packets a player is allowed to send per second. If a client sends more packets,
# all players with the cac.notify permission will be notified.
maxPackets: 80

# The maximum ping a player can have to still be checked
maxPing: 400

# The maximum range a player is allowed to have when hitting an entity.
maxCombatRange: 4

# If set to true players will get banned automatically.
autoBan: true

# The command that will be executed to ban a player.
banCommand: /ban [player]

# The maximum amount of flags a player is allowed to have per cheat before getting banned.
maxFlagsPerModule: 15

# The maximum amount of flags a player is allowed to have before getting banned.
maxFlagsTotal: 30  
```


 
## Comming soon:

-performance improvements

-more blocked cheats

