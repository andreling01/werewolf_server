# werewolf_server

# API List
User | Name | Body | Response | Dev Status |
-----|------|------|---------|-------------|
GameOwner|createGame| gameSetup(String) |roomId(String), 200 | Done
Everyone|joinGame|roomId(String), seatNumber(Int)|CharacterInfo(Object), 200 | Done
GameOwner|startGame|roomId(String)|200(Game Start)/400(Wait for all players) | Done
Everyone|useAbility|roomId(String), seatNumber(Int), targetSeatNumber(Int, Optional, List)|result(Boolean),200
Thief|checkAvailableCharacters|roomId(String), seatNumber(Int),|CharacterInfo(List,Object),200
Thief|pickCharacter|roomId(String),seatNumber(Int), characterIdentity(String)|200
Witch|checkKilledPerson|roomId(String)|seatNumber(Int),200
Witch|useWitchAbility|roomId(String), seatNumber(Int), targetSeatNumber(Int, poison, optional), rescue(Boolean, optional)|200
GameOwner|checkResult|roomId(String)|gameResult(Object), 200
GameOwner|voteResult|roomId(String), seatNumber(Int, List, Optional)|gameResult(Object),200
EveryOne|checkCoupleStatus|roomId(String), seatNumber(Int)|seatNumber(Int),200
GameOwner|checkStatus|roomId(String)|gameStatus(Boolean),200
DeadPeople|checkIdentity|roomId(String)|identityAssignment(Object),200


