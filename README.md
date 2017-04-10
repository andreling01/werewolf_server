# werewolf_server

# API List
User | Name | Body | Response | Dev Status |
-----|------|------|---------|-------------|
GameOwner|createGame| gameSetup(String) |roomId(String), 200 | Done
Everyone | joinRoom | roomId(String), DeviceUUID(String)|num_of_seats(Int), playerSeatNum(Int), 200| Done
Everyone|joinGame|roomId(String), seatNumber(Int), DeviceUUID(String)|CharacterInfo(Object), 200 | Done
GameOwner|startGame|roomId(String), voteResult(Int, List, Optional)|200(Game Start)/400(Wait for all players) | Done
Everyone|useAbility|roomId(String), seatNumber(Int), targetSeatNumber(Int, Optional, List)|result(Boolean),200 | Done
Thief|checkAvailableCharacters|roomId(String), seatNumber(Int),|CharacterInfo(List,Object),200 | Done
Thief|pickCharacter|roomId(String),seatNumber(Int), characterIdentity(String)|200 | Done
Witch|checkKilledPlayer|roomId(String), seatNumber(Int)|seatNumber(Int),200 | Done
Witch|useWitchAbility|roomId(String), seatNumber(Int), targetSeatNumber(Int, poison, optional), rescue(Boolean, optional)|200 | Done
GameOwner|checkResult|roomId(String)|gameResult(Object), 200|Done
EveryOne|checkCoupleStatus|roomId(String), seatNumber(Int)|seatNumber(Int),200|Done
GameOwner|checkStatus|roomId(String)|gameStatus(Boolean),200
DeadPeople|checkIdentity|roomId(String)|identityAssignment(Object),200


