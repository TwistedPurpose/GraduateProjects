//Variation of http://codepen.io/techslides/pen/zowLd

class ImageZoomPan {
    constructor(image) {
        if (image) {
            this.panZoomImage = image;
        } else {
            this.panZoomImage = new Image();
        }

        let self = this;

        self.canvas = $('#mapCanvas')[0];
        self.ctx = self.canvas.getContext('2d');

        // Make this dynamic because... it'll screw up zoom if you don't
        self.canvas.height = $('#mapCanvas').height();
        self.canvas.width = $('#mapCanvas').width();

        self.trackTransforms();

        self.redraw();

        self.lastX = self.canvas.width/2;
        self.lastY = self.canvas.height/2;

        self.canvas.addEventListener('mousedown', function (evt) {
            document.body.style.mozUserSelect = document.body.style.webkitUserSelect = document.body.style.userSelect = 'none';
            self.lastX = evt.offsetX || (evt.pageX - self.canvas.offsetLeft);
            self.lastY = evt.offsetY || (evt.pageY - self.canvas.offsetTop);
            self.dragStart = self.ctx.transformedPoint(self.lastX, self.lastY);
            self.dragged = false;
        }, false);

        self.canvas.addEventListener('mousemove', function (evt) {
            self.lastX = evt.offsetX || (evt.pageX - self.canvas.offsetLeft);
            self.lastY = evt.offsetY || (evt.pageY - self.canvas.offsetTop);
            self.dragged = true;
            if (self.dragStart) {
                var pt = self.ctx.transformedPoint(self.lastX, self.lastY);
                self.ctx.translate(pt.x - self.dragStart.x, pt.y - self.dragStart.y);
                self.redraw();
            }
        }, false);

        self.canvas.addEventListener('mouseup', function (evt) {
            self.dragStart = null;
            if (!self.dragged) zoom(evt.shiftKey ? -1 : 1);
        }, false);

        var scaleFactor = 1.01;

        var zoom = function (clicks) {
            var pt = self.ctx.transformedPoint(self.lastX, self.lastY);
            self.ctx.translate(pt.x, pt.y);
            var factor = Math.pow(scaleFactor, clicks);
            self.ctx.scale(factor, factor);
            self.ctx.translate(-pt.x, -pt.y);
            self.redraw();
        }

        var handleScroll = function (evt) {
            var delta = evt.wheelDelta ? evt.wheelDelta / 40 : evt.detail ? -evt.detail : 0;
            if (delta) zoom(delta);
            return evt.preventDefault() && false;
        };

        self.canvas.addEventListener('DOMMouseScroll', handleScroll, false);
        self.canvas.addEventListener('mousewheel', handleScroll, false);


    }

    redraw() {
        let self = this;

        // Clear the entire canvas
        var p1 = self.ctx.transformedPoint(0, 0);
        var p2 = self.ctx.transformedPoint(self.canvas.width, self.canvas.height);
        self.ctx.clearRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);

        self.ctx.save();
        self.ctx.setTransform(1, 0, 0, 1, 0, 0);
        self.ctx.clearRect(0, 0, self.canvas.width, self.canvas.height);
        self.ctx.restore();

        self.ctx.drawImage(self.panZoomImage, 0, 0, self.canvas.width, self.canvas.height);
    }

    // Adds ctx.getTransform() - returns an SVGMatrix
    // Adds ctx.transformedPoint(x,y) - returns an SVGPoint
    trackTransforms() {
        let self = this;

        var svg = document.createElementNS("http://www.w3.org/2000/svg", 'svg');
        var xform = svg.createSVGMatrix();
        self.ctx.getTransform = function () { return xform; };

        var savedTransforms = [];
        var save = self.ctx.save;
        self.ctx.save = function () {
            savedTransforms.push(xform.translate(0, 0));
            return save.call(self.ctx);
        };

        var restore = self.ctx.restore;
        self.ctx.restore = function () {
            xform = savedTransforms.pop();
            return restore.call(self.ctx);
        };

        var scale = self.ctx.scale;
        self.ctx.scale = function (sx, sy) {
            xform = xform.scaleNonUniform(sx, sy);
            return scale.call(self.ctx, sx, sy);
        };

        var rotate = self.ctx.rotate;
        self.ctx.rotate = function (radians) {
            xform = xform.rotate(radians * 180 / Math.PI);
            return rotate.call(self.ctx, radians);
        };

        var translate = self.ctx.translate;
        self.ctx.translate = function (dx, dy) {
            xform = xform.translate(dx, dy);
            return translate.call(self.ctx, dx, dy);
        };

        var transform = self.ctx.transform;
        self.ctx.transform = function (a, b, c, d, e, f) {
            var m2 = svg.createSVGMatrix();
            m2.a = a; m2.b = b; m2.c = c; m2.d = d; m2.e = e; m2.f = f;
            xform = xform.multiply(m2);
            return transform.call(self.ctx, a, b, c, d, e, f);
        };

        var setTransform = self.ctx.setTransform;
        self.ctx.setTransform = function (a, b, c, d, e, f) {
            xform.a = a;
            xform.b = b;
            xform.c = c;
            xform.d = d;
            xform.e = e;
            xform.f = f;
            return setTransform.call(self.ctx, a, b, c, d, e, f);
        };

        var pt = svg.createSVGPoint();
        self.ctx.transformedPoint = function (x, y) {
            pt.x = x; pt.y = y;
            return pt.matrixTransform(xform.inverse());
        }
    }

}


// 	var canvas = document.getElementsByTagName('canvas')[0];
// 	canvas.width = 800;
// canvas.height = 600;

// 	var gkhead = new Image;

// 	window.onload = function(){		
    
// 		    var ctx = canvas.getContext('2d');
// 		    trackTransforms(ctx);
		  
//     function redraw(){

//           // Clear the entire canvas
//           var p1 = ctx.transformedPoint(0,0);
//           var p2 = ctx.transformedPoint(canvas.width,canvas.height);
//           ctx.clearRect(p1.x,p1.y,p2.x-p1.x,p2.y-p1.y);

//           ctx.save();
//           ctx.setTransform(1,0,0,1,0,0);
//           ctx.clearRect(0,0,canvas.width,canvas.height);
//           ctx.restore();

//           ctx.drawImage(gkhead,0,0);

//         }
//         redraw();

//       var lastX=canvas.width/2, lastY=canvas.height/2;

//       var dragStart,dragged;

//       canvas.addEventListener('mousedown',function(evt){
//           document.body.style.mozUserSelect = document.body.style.webkitUserSelect = document.body.style.userSelect = 'none';
//           lastX = evt.offsetX || (evt.pageX - canvas.offsetLeft);
//           lastY = evt.offsetY || (evt.pageY - canvas.offsetTop);
//           dragStart = ctx.transformedPoint(lastX,lastY);
//           dragged = false;
//       },false);

//       canvas.addEventListener('mousemove',function(evt){
//           lastX = evt.offsetX || (evt.pageX - canvas.offsetLeft);
//           lastY = evt.offsetY || (evt.pageY - canvas.offsetTop);
//           dragged = true;
//           if (dragStart){
//             var pt = ctx.transformedPoint(lastX,lastY);
//             ctx.translate(pt.x-dragStart.x,pt.y-dragStart.y);
//             redraw();
//                 }
//       },false);

//       canvas.addEventListener('mouseup',function(evt){
//           dragStart = null;
//           if (!dragged) zoom(evt.shiftKey ? -1 : 1 );
//       },false);

//       var scaleFactor = 1.1;

//       var zoom = function(clicks){
//           var pt = ctx.transformedPoint(lastX,lastY);
//           ctx.translate(pt.x,pt.y);
//           var factor = Math.pow(scaleFactor,clicks);
//           ctx.scale(factor,factor);
//           ctx.translate(-pt.x,-pt.y);
//           redraw();
//       }

//       var handleScroll = function(evt){
//           var delta = evt.wheelDelta ? evt.wheelDelta/40 : evt.detail ? -evt.detail : 0;
//           if (delta) zoom(delta);
//           return evt.preventDefault() && false;
//       };
    
//       canvas.addEventListener('DOMMouseScroll',handleScroll,false);
//       canvas.addEventListener('mousewheel',handleScroll,false);
// 	};

// 	gkhead.src = 'http://phrogz.net/tmp/gkhead.jpg';
	
// 	// Adds ctx.getTransform() - returns an SVGMatrix
// 	// Adds ctx.transformedPoint(x,y) - returns an SVGPoint
// 	function trackTransforms(ctx){
//       var svg = document.createElementNS("http://www.w3.org/2000/svg",'svg');
//       var xform = svg.createSVGMatrix();
//       ctx.getTransform = function(){ return xform; };

//       var savedTransforms = [];
//       var save = ctx.save;
//       ctx.save = function(){
//           savedTransforms.push(xform.translate(0,0));
//           return save.call(ctx);
//       };
    
//       var restore = ctx.restore;
//       ctx.restore = function(){
//         xform = savedTransforms.pop();
//         return restore.call(ctx);
// 		      };

//       var scale = ctx.scale;
//       ctx.scale = function(sx,sy){
//         xform = xform.scaleNonUniform(sx,sy);
//         return scale.call(ctx,sx,sy);
// 		      };
    
//       var rotate = ctx.rotate;
//       ctx.rotate = function(radians){
//           xform = xform.rotate(radians*180/Math.PI);
//           return rotate.call(ctx,radians);
//       };
    
//       var translate = ctx.translate;
//       ctx.translate = function(dx,dy){
//           xform = xform.translate(dx,dy);
//           return translate.call(ctx,dx,dy);
//       };
    
//       var transform = ctx.transform;
//       ctx.transform = function(a,b,c,d,e,f){
//           var m2 = svg.createSVGMatrix();
//           m2.a=a; m2.b=b; m2.c=c; m2.d=d; m2.e=e; m2.f=f;
//           xform = xform.multiply(m2);
//           return transform.call(ctx,a,b,c,d,e,f);
//       };
    
//       var setTransform = ctx.setTransform;
//       ctx.setTransform = function(a,b,c,d,e,f){
//           xform.a = a;
//           xform.b = b;
//           xform.c = c;
//           xform.d = d;
//           xform.e = e;
//           xform.f = f;
//           return setTransform.call(ctx,a,b,c,d,e,f);
//       };
    
//       var pt  = svg.createSVGPoint();
//       ctx.transformedPoint = function(x,y){
//           pt.x=x; pt.y=y;
//           return pt.matrixTransform(xform.inverse());
//       }
// 	}