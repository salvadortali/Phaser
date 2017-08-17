
// -- user code here --

/* --- start generated code --- */

// Generated by Phaser Editor v1.4.2 (Phaser v2.6.2)


class Dino extends Phaser.Sprite {
	/**
	 * Dino.
	 * @param {Phaser.Game} aGame A reference to the currently running game.
	 * @param {number} aX The x coordinate (in world space) to position the Sprite at.
	 * @param {number} aY The y coordinate (in world space) to position the Sprite at.
	 * @param {any} aKey This is the image or texture used by the Sprite during rendering. It can be a string which is a reference to the Cache entry, or an instance of a RenderTexture or PIXI.Texture.
	 * @param {any} aFrame If this Sprite is using part of a sprite sheet or texture atlas you can specify the exact frame to use by giving a string or numeric index.
	 */
	constructor(aGame, aX, aY, aKey, aFrame) {
		
		super(aGame, aX, aY, aKey === undefined? 'dino' : aKey, aFrame === undefined? 3 : aFrame);
		var _anim_walk = this.animations.add('walk', [0, 1], 4, true);
		var _anim_stay = this.animations.add('stay', [3], 60, false);
		var _anim_jump = this.animations.add('jump', [2], 60, false);
		
		// public fields
		
		this.fAnim_walk = _anim_walk;
		this.fAnim_stay = _anim_stay;
		this.fAnim_jump = _anim_jump;
		
	}
	
	/* sprite-methods-begin */
	// -- user code here --
	/* sprite-methods-end */
}
/* --- end generated code --- */
// -- user code here --
