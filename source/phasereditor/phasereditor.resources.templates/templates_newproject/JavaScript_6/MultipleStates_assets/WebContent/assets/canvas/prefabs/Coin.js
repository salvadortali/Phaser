
// -- user code here --

/* --- start generated code --- */

// Generated by Phaser Editor v1.4.2 (Phaser v2.6.2)


class Coin extends Phaser.Sprite {
	/**
	 * Coin.
	 * @param {Phaser.Game} aGame A reference to the currently running game.
	 * @param {number} aX The x coordinate (in world space) to position the Sprite at.
	 * @param {number} aY The y coordinate (in world space) to position the Sprite at.
	 * @param {any} aKey This is the image or texture used by the Sprite during rendering. It can be a string which is a reference to the Cache entry, or an instance of a RenderTexture or PIXI.Texture.
	 * @param {any} aFrame If this Sprite is using part of a sprite sheet or texture atlas you can specify the exact frame to use by giving a string or numeric index.
	 */
	constructor(aGame, aX, aY, aKey, aFrame) {
		
		super(aGame, aX, aY, aKey === undefined? 'coins' : aKey, aFrame === undefined? 0 : aFrame);
		this.scale.setTo(0.5, 0.5);
		var _anim_rotate = this.animations.add('rotate', [0, 1, 2, 3, 4, 5], 5, true);
		
		// public fields
		
		this.fCoins = this;
		this.fAnim_rotate = _anim_rotate;
		
		// my code after objects creation
		this.afterCreate();
		
	}
	
	/* sprite-methods-begin */
	
	afterCreate() {
		this.fAnim_rotate.play();
	}
	
	/* sprite-methods-end */
}
/* --- end generated code --- */
// -- user code here --
