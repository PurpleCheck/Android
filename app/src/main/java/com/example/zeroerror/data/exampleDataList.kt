package com.example.zeroerror.data

import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order

class exampleDataList {
    companion object{
        val order_2277017440123 = listOf(
            Order(8809612864084, "이니스프리", "그린티 퓨어 젤 핸드크림",1,0, false),
            Order(8809320938893, "더프트앤도프트", "엔젤스코튼 너리싱 핸드크림",2,0, false),
            Order(8801067085601, "모나미", "네임펜 Black",3,0, false),
            Order(3661434000683, "유리아쥬", "스틱레브르 오리지널",1,0, false),
        )
        val inspectList = listOf(
            Inspect(1, 2277017440123, "210-R0-2277017440123-0001", order_2277017440123, 7, 0))
    }
}