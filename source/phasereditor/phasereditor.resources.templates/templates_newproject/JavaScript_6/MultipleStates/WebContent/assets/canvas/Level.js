
// -- user code here --

/* --- start generated code --- */

// Generated by Phaser Editor v1.4.2 (Phaser v2.6.2)


class Level extends Phaser.State {
	
	/**
	 * Level.
	 */
	constructor() {
		
		super();
		
	}
	
	init() {
		
		this.stage.backgroundColor = '#ff8000';
		
	}
	
	preload () {
		
	}
	
	create() {
		this.initObjects();
		
	}
	
	/* state-methods-begin */
	
	initObjects() {
		this.add.text(100, 100, "You are playing now!", { fill:"#000" });
	}
	
	/* state-methods-end */
	
}
/* --- end generated code --- */
// -- user code here --
