package ru.atom.game;

import org.junit.Ignore;
import org.junit.Test;
import ru.atom.game.geometry.Bar;
import ru.atom.game.geometry.Collider;
import ru.atom.game.geometry.Point;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BarBarCollisionTest {
    @Test
    public void barSelfCollide() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar.isColliding(bar), is(true));
    }

    @Test
    public void barsEquals() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.equals(bar2), is(true));
    }

    @Test
    public void barIsNotOriented1() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(100, 100), new Point(0, 0));
        assertThat(bar1.equals(bar2), is(true));
    }

    @Test
    public void barIsNotOriented2() {
        Collider bar1 = new Bar(new Point(0, 100), new Point(100, 0));
        Collider bar2 = new Bar(new Point(100, 100), new Point(0, 0));
        assertThat(bar1.equals(bar2), is(true));
    }

    @Test
    public void barIsNotOriented3() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(100, 0), new Point(0, 100));
        assertThat(bar1.equals(bar2), is(true));
    }

    @Test
    public void equalBarsCollide1() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void equalBarsCollide2() {
        Collider bar1 = new Bar(new Point(0, 100), new Point(100, 0));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void equalBarsCollide3() {
        Collider bar1 = new Bar(new Point(100, 0), new Point(0, 100));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void pointsNotCollide1() {
        Collider point1 = new Point(200, 100);
        Collider point2 = new Point(100, 100);
        assertThat(point1.isColliding(point2), is(false));
    }

    @Test
    public void pointsNotCollide2() {
        Collider point1 = new Point(100, 100);
        Collider point2 = new Point(200, 100);
        assertThat(point1.isColliding(point2), is(false));
    }

    @Test
    public void pointsNotCollide3() {
        Collider point1 = new Point(100, 100);
        Collider point2 = new Point(200, 200);
        assertThat(point1.isColliding(point2), is(false));
    }


    @Test
    public void pointInsideBar() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(50, 50);
        assertThat(bar.isColliding(point), is(true));
    }

    @Test
    public void pointOnCornerOfBar() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(0, 0);
        assertThat(bar.isColliding(point), is(true));
    }

    @Test
    public void pointOnBorderOfBar() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(0, 50);
        assertThat(bar.isColliding(point), is(true));
    }

    @Test
    public void pointOutsideOfBar1() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(0, 150);
        assertThat(bar.isColliding(point), is(false));
    }

    @Test
    public void pointOutsideOfBar2() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(150, 0);
        assertThat(bar.isColliding(point), is(false));
    }

    @Test
    public void pointOutsideOfBar3() {
        Collider bar = new Bar(new Point(0, 0), new Point(100, 100));
        Collider point = new Point(150, 150);
        assertThat(bar.isColliding(point), is(false));
    }

    @Test
    public void barIntersectsBar1() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(50, 0), new Point(150, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBar2() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(0, 50), new Point(100, 150));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBar3() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(50, 50), new Point(150, 150));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBar14() {
        Collider bar1 = new Bar(new Point(50, 0), new Point(150, 100));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBar5() {
        Collider bar1 = new Bar(new Point(0, 50), new Point(100, 150));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBar6() {
        Collider bar1 = new Bar(new Point(50, 50), new Point(150, 150));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBarByBorder1() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(0, 100), new Point(100, 200));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIntersectsBarByCorner() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(100, 100), new Point(200, 200));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barIncludesBar() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(200, 200));
        Collider bar2 = new Bar(new Point(50, 50), new Point(150, 150));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barDoesNotIntersectBar1() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(0, 150), new Point(100, 250));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar2() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(150, 0), new Point(250, 100));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar3() {
        Collider bar1 = new Bar(new Point(0, 0), new Point(100, 100));
        Collider bar2 = new Bar(new Point(150, 150), new Point(200, 200));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar4() {
        Collider bar1 = new Bar(new Point(0, 150), new Point(100, 250));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar5() {
        Collider bar1 = new Bar(new Point(150, 0), new Point(250, 100));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar6() {
        Collider bar1 = new Bar(new Point(150, 150), new Point(200, 200));
        Collider bar2 = new Bar(new Point(0, 0), new Point(100, 100));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barDoesNotIntersectBar7() {
        Collider bar1 = new Bar(new Point(32, 96), new Point(64, 128));
        Collider bar2 = new Bar(new Point(32, 32), new Point(64, 64));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barIntersectBar7() {
        Collider bar1 = new Bar(new Point(0, 32), new Point(32, 64));
        Collider bar2 = new Bar(new Point(31, 32), new Point(63, 64));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barDoesNotIntersectBar8() {
        Collider bar1 = new Bar(new Point(64, 32), new Point(64, 96));
        Collider bar2 = new Bar(new Point(31, 32), new Point(63, 64));
        assertThat(bar1.isColliding(bar2), is(false));
    }

    @Test
    public void barIntersectBar8() {
        Collider bar1 = new Bar(new Point(128, 64), new Point(160, 96));
        Collider bar2 = new Bar(new Point(128, 33), new Point(128, 65));
        assertThat(bar1.isColliding(bar2), is(true));
    }

    @Test
    public void barDoesNotIntersectBar9() {
        Collider bar1 = new Bar(new Point(64, 64), new Point(96, 96));
        Collider bar2 = new Bar(new Point(37, 34), new Point(65, 62));
        assertThat(bar1.isColliding(bar2), is(false));
    }
}
