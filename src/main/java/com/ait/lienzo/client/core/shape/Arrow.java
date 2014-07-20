/*
   Copyright (c) 2014 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.shape;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.shape.json.IFactory;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.client.core.util.GeometryException;
import com.ait.lienzo.shared.core.types.ArrowType;
import com.ait.lienzo.shared.core.types.ShapeType;
import com.google.gwt.json.client.JSONObject;

/**
 * Arrow is a type of Polygon. A picture is worth more than a 1000 words:
 * <pre>
 *                   .          ^
 *                   |\         |  
 * ^  +--------------+ \        |
 * |  |                 \       |
 * W  S              ___ \ E    AW
 * |  |                 a/      |
 * v  +--------------+  /       |
 *                   |b/        |
 *                   ./         v
 * </pre>
 * 
 * Arrow goes from Start point (S) to End point (E). 
 * The baseWidth is W, the headWidth is AW.
 * The angle between the midline and the outer diagonal is called arrowAngle (a), e.g. 45 degrees.
 * The angle between the outer diagonal and the inner diagonal is called baseAngle (b), e.g. 45 degrees.
 * <p>
 * If arrowType is AT_START, then there's an arrow head at point (S).
 * If arrowType is AT_END, then there's an arrow head at point (E).
 * If arrowType is AT_BOTH_ENDS, then there's an arrow head at point (S) and at point (E).
 * AT_END_TAPERED is similar to AT_END, but the base of the arrow tapers off as it gets closer to (S)
 * and both lines of the base meet in point (S). 
 * (Similarly for AT_START_TAPERED.)
 */
public class Arrow extends Shape<Arrow>
{
    private Point2DArray m_polygon;

    /**
     * Constructor. Creates an instance of an arrow.
     * 
     * @param start 
     * @param end
     * @param baseWidth
     * @param headWidth
     * @param arrowAngle
     * @param baseAngle
     * @param arrowType
     */
    public Arrow(Point2D start, Point2D end, double baseWidth, double headWidth, double arrowAngle, double baseAngle, ArrowType arrowType)
    {
        super(ShapeType.ARROW);

        setPoints(new Point2DArray(start, end));

        setBaseWidth(baseWidth);

        setHeadWidth(headWidth);

        setArrowAngle(arrowAngle);

        setBaseAngle(baseAngle);

        setArrowType(arrowType);
    }

    /**
     * Constructor. Creates an instance of an arrow that is a copy of the serialized arrow
     * given as an argument.
     * 
     * @param node serialized arrow 
     */
    protected Arrow(JSONObject node, ValidationContext ctx) throws ValidationException
    {
        super(ShapeType.ARROW, node, ctx);
    }

    /**
     * Draws this arrow.
     * 
     * @param context the {@link Context2D} used to draw this arrow.
     */
    @Override
    public boolean prepare(Context2D context, Attributes attr, double alpha)
    {
        Point2DArray list = getPolygon(); // is null for invalid arrow definition

        if ((null != list) && (list.getLength() > 2))
        {
            Point2D point = list.getPoint(0);

            context.beginPath();

            context.moveTo(point.getX(), point.getY());

            final int leng = list.getLength();

            for (int i = 1; i < leng; i++)
            {
                point = list.getPoint(i);

                context.lineTo(point.getX(), point.getY());
            }
            context.closePath();

            return true;
        }
        return false;
    }

    /**
     * Gets the start point of this arrow.
     * 
     * @return Point2D
     */
    public Point2D getStart()
    {
        return getPoints().getPoint(0);
    }

    /**
     * Sets the start point of the arrow.
     * 
     * @param start
     * @return this Arrow
     */
    public Arrow setStart(Point2D start)
    {
        getPoints().setPoint(0, start);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the end point of this arrow.
     * 
     * @return Point2D
     */
    public Point2D getEnd()
    {
        return getPoints().getPoint(1);
    }

    /**
     * Sets the end point of the arrow.
     * 
     * @param end
     * @return this Arrow
     */
    public Arrow setEnd(Point2D end)
    {
        getPoints().setPoint(1, end);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the end-points of this arrow.
     * Point[0] is the start point and point[1] is the end point.
     * 
     * @return Point2DArray
     */
    public Point2DArray getPoints()
    {
        return getAttributes().getPoints();
    }

    /**
     * Sets the end-points of this arrow.  The points should be a 2-element {@link Point2DArray}
     * 
     * @param points Point2DArray
     * @return this Arrow
     */
    public Arrow setPoints(Point2DArray points)
    {
        getAttributes().setPoints(points);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the width of the base of the arrow.
     * 
     * @return double
     */
    public double getBaseWidth()
    {
        return getAttributes().getBaseWidth();
    }

    /**
     * Sets the width of the base of the arrow.
     * 
     * @param baseWidth
     * @return this Arrow
     */
    public Arrow setBaseWidth(double baseWidth)
    {
        getAttributes().setBaseWidth(baseWidth);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the width of the arrow head.
     * 
     * @return double
     */
    public double getHeadWidth()
    {
        return getAttributes().getHeadWidth();
    }

    /**
     * Sets the width of the arrow head.
     * 
     * @param headWidth
     * @return this Arrow
     */
    public Arrow setHeadWidth(double headWidth)
    {
        getAttributes().setHeadWidth(headWidth);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the angle between the midline of the arrow (between start and end points)
     * and the outer diagonal.
     * 
     * @return angle in degrees, e.g. 45.0
     */
    public double getArrowAngle()
    {
        return getAttributes().getArrowAngle();
    }

    /**
     * Sets the angle between the midline of the arrow (between start and end points)
     * and the outer diagonal.
     * 
     * @param arrowAngle in degrees, e.g. 45.0
     * @return this Arrow
     */
    public Arrow setArrowAngle(double arrowAngle)
    {
        getAttributes().setArrowAngle(arrowAngle);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the angle between the outer diagonal and the inner diagonal 
     * of the arrow head.
     * 
     * @return angle in degrees, e.g. 45.0
     */
    public double getBaseAngle()
    {
        return getAttributes().getBaseAngle();
    }

    /**
     * Sets the angle between the outer diagonal and the inner diagonal 
     * of the arrow head.
     * 
     * @param baseAngle in degrees, e.g. 45.0
     * @return this Arrow
     */
    public Arrow setBaseAngle(double baseAngle)
    {
        getAttributes().setBaseAngle(baseAngle);

        invalidatePolygon();

        return this;
    }

    /**
     * Gets the arrow type which specifies if the arrow has one or two arrow heads,
     * where they are located and whether the end without the arrow head is tapered or not.
     * 
     * @return ArrowType
     */
    public ArrowType getArrowType()
    {
        return getAttributes().getArrowType();
    }

    /**
     * Sets the arrow type which specifies if the arrow has one or two arrow heads,
     * where they are located and whether the end without the arrow head is tapered or not.
     * 
     * @param end
     * @return this Arrow
     */
    public Arrow setArrowType(ArrowType arrowType)
    {
        getAttributes().setArrowType(arrowType);

        invalidatePolygon();

        return this;
    }

    private void invalidatePolygon()
    {
        m_polygon = null;
    }

    private Point2DArray getPolygon()
    {
        /*
         * 4 ^
         * |\ |
         * ^ 6--------------5 \ |
         * | | \ |
         * W S ___ \ E=3 AW
         * | | a/ |
         * v 0--------------1 / |
         * | / |
         * 2/ v
         * ^
         * |
         * dy
         * dx ->
         */

        if (m_polygon == null)
        {
            Point2DArray arr = new Point2DArray();
            try
            {
                ArrowType type = getArrowType();
                double a = Geometry.toRadians(getArrowAngle());
                double sina = Math.sin(a);
                double cosa = Math.cos(a);
                // NOTE: b is not the base angle here, it's the corner EAB
                // i.e. going from E to A to B, where B is point[2] and A is the
                // intersection of the midline (thru S and E)
                // and the line thru point [1] and [2].
                double b_degrees = 180 - getBaseAngle() - getArrowAngle();
                double b = Geometry.toRadians(b_degrees);
                double sinb = Math.sin(b);
                double cosb = Math.cos(b);
                double w = getBaseWidth();
                double aw = getHeadWidth();

                Point2D s = getStart(); // arr.getPoint(0);
                Point2D e = getEnd(); // arr.getPoint(1);
                Point2D dv = e.minus(s);
                Point2D dx = dv.unit(); // unit vector in the direction of SE
                Point2D dy = dx.perpendicular();

                if (type == ArrowType.AT_END || type == ArrowType.AT_END_TAPERED || type == ArrowType.AT_BOTH_ENDS)
                {
                    // cosa*r
                    //
                    // S----+---E
                    // | a/ sina*r=aw/2
                    // sina*r | / r
                    // |/
                    // 2
                    //
                    double r = aw / (2 * sina);
                    double z = r * cosa;
                    Point2D p2 = e.minus(dx.times(z)).minus(dy.times(aw / 2));
                    Point2D p4 = e.minus(dx.times(z)).plus(dy.times(aw / 2));

                    // cosb*r2
                    //
                    // 1---+
                    // \b |
                    // \ | sinb*r2=(aw-w)/2
                    // r2 \|
                    // 2
                    //

                    Point2D p1 = p2.plus(dy.times((aw - w) / 2));
                    Point2D p5 = p4.minus(dy.times((aw - w) / 2));
                    if (b_degrees != 90)
                    {
                        double r2 = (aw - w) / (2 * sinb);
                        Point2D d1 = dx.times(r2 * cosb);
                        p1 = p1.minus(d1);
                        p5 = p5.minus(d1);
                    }

                    arr.push(p1);
                    arr.push(p2);
                    arr.push(e);
                    arr.push(p4);
                    arr.push(p5);
                }
                else if (type == ArrowType.AT_START)
                {
                    Point2D q0 = e.plus(dy.times(-w / 2));
                    Point2D q6 = e.plus(dy.times(w / 2));
                    arr.push(q0);
                    arr.push(q6);
                }
                else
                // ArrowType.AT_START_TAPERED
                {
                    arr.push(e);
                }

                if (type == ArrowType.AT_START || type == ArrowType.AT_START_TAPERED || type == ArrowType.AT_BOTH_ENDS)
                {
                    // cosa*r
                    //
                    // S----+---E
                    // | a/ sina*r=aw/2
                    // sina*r | / r
                    // |/
                    // 2
                    //
                    double r = aw / (2 * sina);
                    double z = r * cosa;
                    Point2D q2 = s.plus(dx.times(z)).minus(dy.times(aw / 2));
                    Point2D q4 = s.plus(dx.times(z)).plus(dy.times(aw / 2));

                    // cosb*r2
                    //
                    // 1---+
                    // \b |
                    // \ | sinb*r2=(aw-w)/2
                    // r2 \|
                    // 2
                    //

                    Point2D q1 = q2.plus(dy.times((aw - w) / 2));
                    Point2D q5 = q4.minus(dy.times((aw - w) / 2));
                    if (b_degrees != 90)
                    {
                        double r2 = (aw - w) / (2 * sinb);
                        Point2D d1 = dx.times(r2 * cosb);
                        q1 = q1.plus(d1);
                        q5 = q5.plus(d1);
                    }

                    arr.push(q5);
                    arr.push(q4);
                    arr.push(s);
                    arr.push(q2);
                    arr.push(q1);
                }
                else if (type == ArrowType.AT_END)
                {
                    Point2D p0 = s.plus(dy.times(-w / 2));
                    Point2D p6 = s.plus(dy.times(w / 2));

                    arr.push(p6);
                    arr.push(p0);
                }
                else
                // ArrowType.AT_END_TAPERED
                {
                    arr.push(s);
                }
            }
            catch (GeometryException e)
            {
                // This can happen e.g. when S and E are the same point.
                // Leave m_polygon array empty and the draw code will simply not draw it.
            }
            m_polygon = arr;
        }
        return m_polygon;
    }

    @Override
    public IFactory<Arrow> getFactory()
    {
        return new ArrowFactory();
    }

    public static class ArrowFactory extends ShapeFactory<Arrow>
    {
        public ArrowFactory()
        {
            super(ShapeType.ARROW);

            addAttribute(Attribute.POINTS, true);

            addAttribute(Attribute.BASE_WIDTH, true);

            addAttribute(Attribute.HEAD_WIDTH, true);

            addAttribute(Attribute.ARROW_ANGLE, true);

            addAttribute(Attribute.BASE_ANGLE, true);

            addAttribute(Attribute.ARROW_TYPE, true);
        }

        @Override
        public Arrow create(JSONObject node, ValidationContext ctx) throws ValidationException
        {
            return new Arrow(node, ctx);
        }
    }
}
