package de.mcflux.lobbysystem.mongodb;

public enum ClanRoles {
	
	LEADER, COLEADER, MEMBER, MODERATOR; 
	
	public static ClanRoles getNextRole(ClanRoles role) {
		switch(role) {
		case LEADER:
			return null;
		case MEMBER:
			return MODERATOR;
		case MODERATOR:
			return COLEADER;
		case COLEADER:
			return null;
		default:
			break;
		}
		
		return null; 
	}
	
	public static ClanRoles getPreviousRole(ClanRoles role) {
		switch(role) {
		case LEADER:
			return COLEADER;
		case COLEADER:
			return MODERATOR; 
		case MEMBER:
			return null;
		case MODERATOR:
			return MEMBER;
		default:
			break;
		}
		
		return null; 
	}

}
