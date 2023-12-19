package org.example.tulitskayte_d_v.application;


import org.example.tulitskayte_d_v.model.game.utils.GamePlayer;
import org.example.tulitskayte_d_v.model.player.Player;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //b2-b5, E2-G2, E4-E4, I4-I5, D7-F7, I8-I9, B10-B10, D10-D10, F10-F10, I1-I2
        //a1-a4, c6-c8, f3-f5, i3-j3, i5-j5, i7-j7, a10-a10, c11-c11, e12-e12, g13-g13, i14-i14, k15-k15, l13-l13, m11-m11, n9-n9
        //a7-a10, d1-d4, c11-c11, e13-e13, g15-g15, i17-i17, k18-k18, m20-m20, h3-j3, h8-j8, n2-o2, n5-o5, n8-o8, n11-o11, s3-s3, s5-s5, s7-s7, s9-s9, s11-s11, s13-s13
        //a1-a4, d5-d8, h7-h10, l3-n3, l5-n5, l8-n8, l11-n11, l13-m13, l15-m15, l17-m17, l19-m19, l21-m21, r2-r2, r4-r4, r6-r6, r8-r8, r10-r10, r12-r12, r14-r14, r16-r16, r18-r18, r20-r20, r22-r22, r24-r24, u2-u2
        GamePlayer game = new GamePlayer();

        Player firstPlayer = game.initializePlayer(sc, "Первый игрок");
        Player secondPlayer = game.initializePlayer(sc, "Второй игрок");

        game.startGame(firstPlayer, secondPlayer);
    }
}