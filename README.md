# BlurOverflowText Android Compose

[![](https://jitpack.io/v/idapgroup/BlurOverflowText.svg)](https://jitpack.io/#idapgroup/BlurOverflowText)

Blur overflow text effect for compose enjoyers.
| ![image](https://github.com/idapgroup/BlurOverflowText/assets/12797421/81ba9b5d-3dcc-4f07-96fd-07a389425fb9) | ![image](https://github.com/idapgroup/BlurOverflowText/assets/12797421/10b04ad0-76bc-4b92-b66c-0304e4e95a50) |
| ----------- | ----------- |

## Setup
Please, add to repositories jitpack:
```groovy
repositories {
  mavenCentral()
  ...
  maven { url 'https://jitpack.io' }
}
```
Add to your module next dependency:
```groovy
dependencies {
  implementation 'com.github.idapgroup:BlurOverflowText:<latest-version>'
}
```
`Note:` Do not forget to add compose dependencies 🙃

## Usage sample

`BlurOverflowText` has all the base `Text` parameters except of `overflow` and `softWrap` to avoid unexpected library behavior.

The library works only if `maxLines` parameter is set.

If text exeeds the boundary limit, the last line of the text will be blured. `blurLineWidth` have all responsibilities for this.

Just provide expected blur length (default value is 0.2f which means 20% on the line width).

```kotlin
        BlurOverflowText(
            text = "Some very long string that should overflow this line.",
            modifier = Modifier.align(Alignment.Center),
            maxLines = 1,
            fontSize = 24.sp,
            blurLineWidth = 0.2f,
        )
```
