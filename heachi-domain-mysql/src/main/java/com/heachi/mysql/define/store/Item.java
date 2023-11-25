package com.heachi.mysql.define.store;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.store.constant.ItemStatus;
import com.heachi.mysql.define.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;            // 상품 이름

    @Column(name = "CONTENT")
    private String content;         // 상품 설명

    @Column(name = "PRICE", nullable = false)
    private int price;              // 상품 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;            // 상품 판매자

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @ColumnDefault(value = "'ITEM_AVAILABLE'")
    private ItemStatus itemStatus;  // 상품 상태

    @Column(name = "ITEM_IMAGE_URL", nullable = false)
    private String itemImageURL;    // 상품 이미지 URL

    @Builder
    public Item(String name, String content, int price, User user, ItemStatus itemStatus, String itemImageURL) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.user = user;
        this.itemStatus = itemStatus;
        this.itemImageURL = itemImageURL;
    }
}
