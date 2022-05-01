// Generated by TFLite Support.
package com.dailystudio.tflite.example.medata;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Tensor.QuantizationParams;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.common.ops.DequantizeOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.common.ops.QuantizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.metadata.MetadataExtractor;
import org.tensorflow.lite.support.metadata.schema.NormalizationOptions;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.model.Model.Device;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

/** Wrapper class of model aiy/vision/classifier/food_V1 (Version: 1) */
public class LiteModelFoodV1 {
  private final Metadata metadata;
  private final Model model;
  private static final String MODEL_NAME = "lite-model_aiy_vision_classifier_food_V1_1.tflite";
  private final List<String> probabilityLabels;
  @Nullable private ImageProcessor imagePreprocessor;
  @Nullable private TensorProcessor probabilityPostprocessor;

  /** Input wrapper of {@link LiteModelFoodV1} */
  public class Inputs {
    private TensorImage image;

    public Inputs() {
      Metadata metadata = LiteModelFoodV1.this.metadata;
      image = new TensorImage(metadata.getImageType());
    }

    public void loadImage(Bitmap bitmap) {
      image.load(bitmap);
      image = preprocessImage(image);
    }

    public void loadImage(TensorImage tensorImage) {
      image = preprocessImage(tensorImage);
    }

    private TensorImage preprocessImage(TensorImage tensorImage) {
      if (imagePreprocessor == null) {
        return tensorImage;
      }
      return imagePreprocessor.process(tensorImage);
    }

    Object[] getBuffer() {
      return new Object[] {image.getBuffer()};
    }
  }

  /** Output wrapper of {@link LiteModelFoodV1} */
  public class Outputs {
    private final TensorBuffer probability;

    public Outputs() {
      Metadata metadata = LiteModelFoodV1.this.metadata;
      probability = TensorBuffer.createFixedSize(metadata.getProbabilityShape(), metadata.getProbabilityType());
    }

    public Map<String, Float> getProbability() {
      return new TensorLabel(probabilityLabels, postprocessProbability(probability)).getMapWithFloatValue();
    }

    private TensorBuffer postprocessProbability(TensorBuffer tensorBuffer) {
      if (probabilityPostprocessor == null) {
        return tensorBuffer;
      }
      return probabilityPostprocessor.process(tensorBuffer);
    }

    Map<Integer, Object> getBuffer() {
      Map<Integer, Object> outputs = new HashMap<>();
      outputs.put(0, probability.getBuffer());
      return outputs;
    }
  }

  /** Metadata accessors of {@link LiteModelFoodV1} */
  public static class Metadata {
    private final int[] imageShape;
    private final DataType imageDataType;
    private final QuantizationParams imageQuantizationParams;
    private final float[] imageMean;
    private final float[] imageStddev;
    private final int[] probabilityShape;
    private final DataType probabilityDataType;
    private final QuantizationParams probabilityQuantizationParams;
    private final List<String> probabilityLabels;

    public Metadata(ByteBuffer buffer, Model model) throws IOException {
      MetadataExtractor extractor = new MetadataExtractor(buffer);
      imageShape = extractor.getInputTensorShape(0);
      imageDataType = extractor.getInputTensorType(0);
      imageQuantizationParams = extractor.getInputTensorQuantizationParams(0);
      NormalizationOptions imageNormalizationOptions =
          (NormalizationOptions) extractor.getInputTensorMetadata(0).processUnits(0).options(new NormalizationOptions());
      FloatBuffer imageMeanBuffer = imageNormalizationOptions.meanAsByteBuffer().asFloatBuffer();
      imageMean = new float[imageMeanBuffer.limit()];
      imageMeanBuffer.get(imageMean);
      FloatBuffer imageStddevBuffer = imageNormalizationOptions.stdAsByteBuffer().asFloatBuffer();
      imageStddev = new float[imageStddevBuffer.limit()];
      imageStddevBuffer.get(imageStddev);
      probabilityShape = model.getOutputTensorShape(0);
      probabilityDataType = extractor.getOutputTensorType(0);
      probabilityQuantizationParams = extractor.getOutputTensorQuantizationParams(0);
      String probabilityLabelsFileName =
          extractor.getOutputTensorMetadata(0).associatedFiles(1).name();
      probabilityLabels = FileUtil.loadLabels(extractor.getAssociatedFile(probabilityLabelsFileName));
      Log.d("MODEL", "probabilityLabels: " + probabilityLabels);
    }

    public int[] getImageShape() {
      return Arrays.copyOf(imageShape, imageShape.length);
    }

    public DataType getImageType() {
      return imageDataType;
    }

    public QuantizationParams getImageQuantizationParams() {
      return imageQuantizationParams;
    }

    public float[] getImageMean() {
      return Arrays.copyOf(imageMean, imageMean.length);
    }

    public float[] getImageStddev() {
      return Arrays.copyOf(imageStddev, imageStddev.length);
    }

    public int[] getProbabilityShape() {
      return Arrays.copyOf(probabilityShape, probabilityShape.length);
    }

    public DataType getProbabilityType() {
      return probabilityDataType;
    }

    public QuantizationParams getProbabilityQuantizationParams() {
      return probabilityQuantizationParams;
    }

    public List<String> getProbabilityLabels() {
      return probabilityLabels;
    }
  }

  public Metadata getMetadata() {
    return metadata;
  }

  /**
   * Creates interpreter and loads associated files if needed.
   *
   * @throws IOException if an I/O error occurs when loading the tflite model.
   */
  public LiteModelFoodV1(Context context) throws IOException {
    this(context, MODEL_NAME, Device.CPU, 1);
  }

  /**
   * Creates interpreter and loads associated files if needed, but loading another model in the same
   * input / output structure with the original one.
   *
   * @throws IOException if an I/O error occurs when loading the tflite model.
   */
  public LiteModelFoodV1(Context context, String modelPath) throws IOException {
    this(context, modelPath, Device.CPU, 1);
  }

  /**
   * Creates interpreter and loads associated files if needed, with device and number of threads
   * configured.
   *
   * @throws IOException if an I/O error occurs when loading the tflite model.
   */
  public LiteModelFoodV1(Context context, Device device, int numThreads) throws IOException {
    this(context, MODEL_NAME, device, numThreads);
  }

  /**
   * Creates interpreter for a user-specified model.
   *
   * @throws IOException if an I/O error occurs when loading the tflite model.
   */
  public LiteModelFoodV1(Context context, String modelPath, Device device, int numThreads) throws IOException {
    model = new Model.Builder(context, modelPath).setDevice(device).setNumThreads(numThreads).build();
    metadata = new Metadata(model.getData(), model);

    ImageProcessor.Builder imagePreprocessorBuilder = new ImageProcessor.Builder()
        .add(new ResizeOp(
            metadata.getImageShape()[1],
            metadata.getImageShape()[2],
            ResizeMethod.NEAREST_NEIGHBOR))
        .add(new NormalizeOp(metadata.getImageMean(), metadata.getImageStddev()))
        .add(new QuantizeOp(
            metadata.getImageQuantizationParams().getZeroPoint(),
            metadata.getImageQuantizationParams().getScale()))
        .add(new CastOp(metadata.getImageType()));
    imagePreprocessor = imagePreprocessorBuilder.build();

    TensorProcessor.Builder probabilityPostprocessorBuilder = new TensorProcessor.Builder()
        .add(new DequantizeOp(
            metadata.getProbabilityQuantizationParams().getZeroPoint(),
            metadata.getProbabilityQuantizationParams().getScale()));
    probabilityPostprocessor = probabilityPostprocessorBuilder.build();

    probabilityLabels = metadata.getProbabilityLabels();
  }

  public void resetImagePreprocessor(@Nullable ImageProcessor processor) {
    imagePreprocessor = processor;
  }

  public void resetProbabilityPostprocessor(@Nullable TensorProcessor processor) {
    probabilityPostprocessor = processor;
  }

  /** Creates inputs */
  public Inputs createInputs() {
    return new Inputs();
  }

  /** Triggers the model. */
  public Outputs run(Inputs inputs) {
    Outputs outputs = new Outputs();
    model.run(inputs.getBuffer(), outputs.getBuffer());
    return outputs;
  }

  /** Closes the model. */
  public void close() {
    model.close();
  }
}

