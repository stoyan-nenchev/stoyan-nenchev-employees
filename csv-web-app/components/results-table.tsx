import {EmployeePair} from "@/types/employee";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";

interface ResultsTableProps {
    data: EmployeePair[];
}

export default function ResultsTable({data}: ResultsTableProps) {
    return (
        <Card>
            <CardHeader>
                <CardTitle>Most Collaborative Employee Pairs</CardTitle>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Employee ID #1</TableHead>
                            <TableHead>Employee ID #2</TableHead>
                            <TableHead>Project ID</TableHead>
                            <TableHead>Days Worked</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {data.map((pair, index) => (
                            <TableRow key={index}>
                                <TableCell>{pair.firstEmployeeId}</TableCell>
                                <TableCell>{pair.secondEmployeeId}</TableCell>
                                <TableCell>{pair.projectId}</TableCell>
                                <TableCell>{pair.daysWorked}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    )
}