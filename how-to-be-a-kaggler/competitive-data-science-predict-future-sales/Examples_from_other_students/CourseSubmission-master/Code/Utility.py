import os, sys, time
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error

### Downcasting.
def downcast_dtypes(df):
    float_cols = [c for c in df if df[c].dtype == "float64"]
    int_cols = [c for c in df if df[c].dtype in ["int64", "int32"]]
    df[float_cols] = df[float_cols].astype(np.float32)
    df[int_cols] = df[int_cols].astype(np.int16)
    return df

### Model training.
# Plot training progress of light GBM over number of iterations.
def plot_lgbm_eval_metrics(eval_metrics):
    plt.plot(eval_metrics["train"]["rmse"], label="train")
    plt.plot(eval_metrics["valid"]["rmse"], label="valid")
    plt.title("RMSE")
    plt.xlabel("iteration")
    plt.ylabel("RMSE")
    plt.legend(loc="center right")
    plt.grid(True)

# Show variable importance in a light GBM model.
def show_lgbm_var_imp(model):
    gain = model.feature_importance('gain')
    spl = model.feature_importance("split")
    var_imp = pd.DataFrame({"feature": model.feature_name(),
                            "split": spl,
                            "split_frac": 100 * spl / spl.sum(),
                            "gain": gain,
                            "gain_frac": 100 * gain / gain.sum()}).sort_values("gain", ascending=False)
    return var_imp

### Performance evaluation.
def compute_sum_stat(y, z, w=1):
    # Assume y and z are centered to have means 0.
    if w==1:
        ywy = np.dot(y, y)
        ywz = np.dot(y, z)
        zwz = np.dot(z, z)
    else:
        ywy = np.dot(y, np.multiply(w, y))
        ywz = np.dot(y, np.multiply(w, z))
        zwz = np.dot(z, np.multiply(w, z))
    return ywy, ywz, zwz

def compute_reg_score(y, z):
    ywy, ywz, zwz = compute_sum_stat(y-y.mean(), z-z.mean())
    print('Count Y: {}'.format(y.shape[0]))
    print('Sum Y: {}'.format(y.sum()))
    print('Avg Y: {}'.format(y.mean()))
    print('Avg Z: {}'.format(z.mean()))
    print('r: {}'.format(np.corrcoef(y, z)[0,1]))
    print('R2: {}'.format(r2_score(y, z)))
    print('RMSE: {}'.format(np.sqrt(mean_squared_error(y, z))))
    print('MAE: {}'.format(mean_absolute_error(y, z)))
