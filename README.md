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
- Nofall

Combat:
- Killaura (won't be prevented but registered)
- Fastbow
- Reach

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
minTimeBetweenBowShots: 149  
maxPackets: 80 
maxPing: 300  
maxCombatRange: 4  
```
##### minTimeBetweenBowShots: 
The amount of milliseconds between 2 bow shots
##### maxPackets:
he maximum amount of packets that a client can send in 1 second. If a client sends more packets, all players with the cac.notify
permission will get notified
##### maxPing:
If a player has a higher ping he won't be checked
##### maxCombatRange:
The maximum reach a player can have




 
## Comming soon:

-autobans and autokicks

-performance improvements

-more blocked cheats

