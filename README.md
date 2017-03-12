# werewolf_server

# API List
User | Name | Body | Response
-----|------|------|---------
GameOwner|createGame| gameSetup(String) |roomId(String), 200
Everyone|joinGame|roomId(String), seatNumber(Int)|CharacterInfo(Object), 200
GameOwner|startGame|roomId(String)|200(Game Start)/400(Wait for all players)
Everyone|useAbility|roomId(String), seatNumber(Int), targetSetNumber(Int, Optional)|result(Boolean),200
Thief|checkAvailableCharacters|roomId(String)|CharacterInfo(List,Object),200
Thief|pickCharacter|roomId(String),seatNumber(Int), CharacterId(String)|200
Witch|checkKilledPerson|roomId(String)|seatNumber(Int),200
Witch|useWitchAbility|roomId(String), seatNumber(Int), targetNum(Int, poison, optional), rescue(Boolean, optional)|200
GameOwner|checkResult|roomId(String)|gameResult(Object), 200
GameOwner|voteResult|roomId(String), seatNumber(Int, List, Optional)|gameResult(Object),200
EveryOne|checkCoupleStatus|roomId(String), seatNumber(Int)|seatNumber(Int),200
GameOwner|checkStatus|roomId(String)|gameStatus(Boolean),200
DeadPeople|checkIdentity|roomId(String)|identityAssignment(Object),200


