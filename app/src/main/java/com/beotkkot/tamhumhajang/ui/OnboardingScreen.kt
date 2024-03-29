
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.beotkkot.tamhumhajang.AppState
import com.beotkkot.tamhumhajang.R
import com.beotkkot.tamhumhajang.design.theme.TamhumhajangTheme
import com.beotkkot.tamhumhajang.ui.MAP
import com.beotkkot.tamhumhajang.ui.ONBOARDING

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    appState: AppState,
    nickname: String
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            when (page) {
                0 -> PageOneContent(nickname)
                1 -> PageTwoContent(nickname)
                2 -> PageThreeContent(nickname)
                else -> Text("Unknown page")
            }
        }

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            onClick = {
                appState.navigate(MAP) {
                    popUpTo(ONBOARDING) {
                        inclusive = true
                    }
                }
              },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = TamhumhajangTheme.colors.color_9ddb80,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "탐험하기",
                style = TamhumhajangTheme.typography.title3,
            )
        }
    }
}

@Composable
fun PageOneContent(
    nickname: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(TamhumhajangTheme.colors.color_0fa958)) {
                        append(nickname)
                    }
                    append("님, 환영해요!\n" +
                            "탐험할 준비가 끝났어요")
                },
                style = TamhumhajangTheme.typography.largeTitle.copy(
                    color = TamhumhajangTheme.colors.color_000000
                ),
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(width = 340.dp, height = 340.dp)
                .align(Alignment.CenterHorizontally),
            border = BorderStroke(
                width = 2.dp,
                color = TamhumhajangTheme.colors.color_9ddb80,
            ),
            color = Color.Transparent
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    Text(
                        text = "메인 화면에서 보이는 메뉴들이에요!",
                        style = TamhumhajangTheme.typography.title1,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_star),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "단골",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "단골로 등록한 상점들을 볼 수 있어요.",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_book),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "도감",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "내 등급과 배지 현황을 볼 수 있어요.",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_quest),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "퀘스트",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "풀어야 할 퀘스트들을 볼 수 있어요!",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PageTwoContent(
    nickname: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(TamhumhajangTheme.colors.color_0fa958)) {
                        append(nickname)
                    }
                    append("님, 환영해요!\n" +
                            "탐험할 준비가 끝났어요")
                },
                style = TamhumhajangTheme.typography.largeTitle.copy(
                    color = TamhumhajangTheme.colors.color_000000
                ),
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(width = 340.dp, height = 340.dp)
                .align(Alignment.CenterHorizontally),
            border = BorderStroke(
                width = 2.dp,
                color = TamhumhajangTheme.colors.color_9ddb80,
            ),
            color = Color.Transparent
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    Text(
                        text = "메인 화면에서 보이는 메뉴들이에요!",
                        style = TamhumhajangTheme.typography.title1,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_market),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "추천 시장",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "탐험할 수 있는 주변 시장을 추천해드려요.",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_location),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "현재 위치",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "현재 내 위치로 가고 싶다면, 눌러 보세요.",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.img_onboarding_question_mark),
                            contentDescription = "IC_CLOSE",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "획득할 배지",
                                style = TamhumhajangTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "아이콘 근처에 가면 배지를 획득할 수있어요!",
                                style = TamhumhajangTheme.typography.description2,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PageThreeContent(
    nickname: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(TamhumhajangTheme.colors.color_0fa958)) {
                        append(nickname)
                    }
                    append("님, 환영해요!\n" +
                            "탐험할 준비가 끝났어요")
                },
                style = TamhumhajangTheme.typography.largeTitle.copy(
                    color = TamhumhajangTheme.colors.color_000000
                ),
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(width = 340.dp, height = 340.dp)
                .align(Alignment.CenterHorizontally),
            border = BorderStroke(
                width = 2.dp,
                color = TamhumhajangTheme.colors.color_9ddb80,
            ),
            color = Color.Transparent
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "다음 등급으로 올라가는 방법이에요!",
                        style = TamhumhajangTheme.typography.title1,
                    )

                    Icon(
                        modifier = Modifier
                            .width(180.dp)
                            .height(150.dp)
                            .clickable {
                            }
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.onboarding_character),
                        contentDescription = "IC_CLOSE",
                        tint = Color.Unspecified
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("누적 트로피 ")
                            withStyle(style = SpanStyle(color = TamhumhajangTheme.colors.color_0fa958)) {
                                append("1")
                            }

                            append("개를 모으면  ---> Level 2")
                        },
                        style = TamhumhajangTheme.typography.body1,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("누적 트로피 ")
                            withStyle(style = SpanStyle(color = TamhumhajangTheme.colors.color_0fa958)) {
                                append("2")
                            }

                            append("개를 모으면  ---> Level 3")
                        },
                        style = TamhumhajangTheme.typography.body1,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("누적 트로피 ")
                            withStyle(style = SpanStyle(color = TamhumhajangTheme.colors.color_0fa958)) {
                                append("3")
                            }

                            append("개를 모으면  ---> Level 4")
                        },
                        style = TamhumhajangTheme.typography.body1,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}