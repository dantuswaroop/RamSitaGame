package com.dantu.findingsita.data.entities

import com.dantu.findingsita.R

data class Character(
    val characterId : Int,
    val name : String,
    val type : CharacterType,
    val points : Int,
    val image : Int
)

enum class CharacterType {
    FIND, HIDE, NONE
}

val characters = listOf(
    Character(1,"Ram", CharacterType.FIND, 1000, R.drawable.rama),
    Character(2,"Sita", CharacterType.HIDE, 1000, R.drawable.sita),
    Character(3,"Raavan", CharacterType.NONE, -400, R.drawable.raavana),
    Character(4,"Laxman", CharacterType.NONE, 700, R.drawable.lakshmana),
    Character(7,"Hanuman", CharacterType.NONE, 600, R.drawable.hanuman),
    Character(5,"Bharat", CharacterType.NONE, 600, R.drawable.bharata),
    Character(6,"Satrugan", CharacterType.NONE, 500, R.drawable.sathrughan),
    Character(8,"Dhasharath", CharacterType.NONE, 400, R.drawable.dasharatha),
    Character(9,"Vaali", CharacterType.NONE, -100, R.drawable.vaali),
    Character(10,"Sughreeva", CharacterType.NONE, 300, R.drawable.sughreeva),
    Character(11,"Angadha", CharacterType.NONE, 300, R.drawable.angadha),
    Character(12,"Soorpanakha", CharacterType.NONE, -100, R.drawable.shurpanaka),
    Character(13,"Mandara", CharacterType.NONE, 100, R.drawable.mandhara),
    Character(14,"Keikeyi", CharacterType.NONE, 200, R.drawable.baseline_check_24),
    Character(15,"Indrajit", CharacterType.NONE, -200, R.drawable.baseline_check_24)

)