"use client"

import {useState} from "react";
import {uploadCSV} from "@/lib/api";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";

interface FileUploadProps {
    onUploadStart: () => void;
    onUploadSuccess: (data: any) => void;
    onUploadError: (error: string) => void;
}

export default function FileUpload({onUploadStart, onUploadSuccess, onUploadError}: FileUploadProps) {
    const [file, setFile] = useState<File | null>(null);

    const handleSubmit = async () => {
        if (!file) return;

        onUploadStart();
        try {
            const result = await uploadCSV(file);
            onUploadSuccess(result);
        } catch (error) {
            onUploadError((error as Error).message);
        }
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle>Upload Employee CSV</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
                <Input
                    type="file"
                    accept=".csv"
                    onChange={(e) => {
                        setFile(e.target.files?.[0] || null);
                    }}
                />
                <Button
                    onClick={handleSubmit}
                    disabled={!file}
                    className="w-full"
                >
                    Analyze Time Overlap
                </Button>
            </CardContent>
        </Card>
    )
}