package cnuphys.ced.component;

public class GeoDisplayBits {

	public static final int REGION_1 = 01;

	public static final int REGION_2 = 02;

	public static final int REGION_3 = 04;

	public static final int REGION_4 = 010;

	public static final int LAYER_1 = 020;

	public static final int LAYER_2 = 040;

	public static final int LAYER_3 = 0100;

	public static final int LAYER_4 = 0200;

	public static final int LAYER_5 = 0400;

	public static final int LAYER_6 = 01000;

	public static final int FMT_REGIONS = REGION_1 | REGION_2 | REGION_3 | REGION_4;

	public static final int FMT_LAYERS = LAYER_1 | LAYER_2 | LAYER_3 | LAYER_4 | LAYER_5 | LAYER_6;

}
