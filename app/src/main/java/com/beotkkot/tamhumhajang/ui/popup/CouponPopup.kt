package com.beotkkot.tamhumhajang.ui.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beotkkot.tamhumhajang.R
import com.beotkkot.tamhumhajang.design.theme.TamhumhajangTheme
import kotlinx.coroutines.delay

@Composable
fun CouponPopup(
    rewardId: Int,
    onClose: (Int) -> Unit = { }
) {
    var isButtonClicked by remember { mutableStateOf(false) }

    LaunchedEffect(isButtonClicked) {
        if (isButtonClicked) {
            delay(3000)
            onClose(rewardId)
        }
    }

    Dialog(
        onDismissRequest = { onClose(rewardId) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TamhumhajangTheme.colors.color_ffffff
        ) {
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .padding(
                        top = 20.dp,
                        bottom = 25.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .background(
                        color = TamhumhajangTheme.colors.color_ffffff,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            end = 20.dp
                        )
                        .align(
                            Alignment.TopEnd
                        )
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                                .clip(CircleShape)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_circle),
                            contentDescription = "IC_CLOSE"
                        )
                        Spacer(
                            modifier = Modifier
                                .width(30.dp)
                                .padding(top = 20.dp)
                        )
                        Text(
                            text = "상인회에 쿠폰을 보여주세요!",
                            style = TamhumhajangTheme.typography.description2.copy(
                                color = TamhumhajangTheme.colors.color_000000
                            )
                        )
                        Spacer(
                            modifier = Modifier
                                .width(30.dp)
                                .padding(top = 20.dp)
                        )
                        Icon(
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                                .clip(CircleShape)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_circle),
                            contentDescription = "IC_CLOSE"
                        )
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "시장상인회\n상품 교환 쿠폰",
                        style = TamhumhajangTheme.typography.title1Description.copy(
                            color = TamhumhajangTheme.colors.color_000000
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(280.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isButtonClicked) Color(0xFFD9D9D9) else Color(0xFFAC93F4),
                            contentColor = TamhumhajangTheme.colors.color_ffffff
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        onClick = {
                            isButtonClicked = true
                        }
                    ) {
                        Text(
                            text = if (isButtonClicked) "트로피 사용 완료" else "트로피 사용하기",
                            style = TamhumhajangTheme.typography.body2,
                            color = TamhumhajangTheme.colors.color_ffffff
                        )
                    }
                }
            }
        }
    }
}